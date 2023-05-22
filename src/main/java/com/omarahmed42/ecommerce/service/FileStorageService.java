package com.omarahmed42.ecommerce.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.omarahmed42.ecommerce.DTO.AttachmentInfo;

public interface FileStorageService {
    AttachmentInfo save(MultipartFile file, String path);

    Resource load(String filename);

    void deleteFile(String path);

    void deleteAll();

    Stream<Path> loadAll();
}
