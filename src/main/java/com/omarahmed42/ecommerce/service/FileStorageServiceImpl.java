package com.omarahmed42.ecommerce.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.omarahmed42.ecommerce.DTO.AttachmentInfo;
import com.omarahmed42.ecommerce.exception.StorageException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final Path root = Paths.get("uploads");

    @Override
    public AttachmentInfo save(MultipartFile file, String path) {
        try {
            Path filePath = this.root.resolve(path);
            Path directoryPath = this.root.resolve(FilenameUtils.getFullPath(path));
            if (Files.notExists(directoryPath))
                Files.createDirectory(directoryPath);
            Files.copy(file.getInputStream(), filePath);
            return new AttachmentInfo(FilenameUtils.getBaseName(path), file.getSize(), filePath.toString());
        } catch (Exception e) {
            log.error("Failed to store file: ", e);
            throw new StorageException("Failed to store file: ", e);
        }
    }

    @Override
    public Resource load(String path) {
        try {
            Path file = root.resolve(path);
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists())
                throw new StorageException("File not found");
            else if (!resource.isReadable())
                throw new StorageException("File is not readable");
            return resource;
        } catch (MalformedURLException e) {
            throw new StorageException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String path) {
        Path filePath = this.root.resolve(path);
        if (Files.isRegularFile(filePath))
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                log.error("File deletion failed: ", e);
                throw new StorageException("File deletion failed", e);
            }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            log.error("Failed to load files: ", e);
            throw new StorageException("Failed to load files", e);
        }
    }
}
