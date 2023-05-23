package com.omarahmed42.ecommerce.service;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.omarahmed42.ecommerce.DTO.AttachmentInfo;
import com.omarahmed42.ecommerce.enums.Role;
import com.omarahmed42.ecommerce.exception.ProductMediaNotFoundException;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.exception.UnauthorizedAccessException;
import com.omarahmed42.ecommerce.exception.UnsupportedFileExtensionException;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.ProductMedia;
import com.omarahmed42.ecommerce.repository.ProductMediaRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import com.omarahmed42.ecommerce.util.UserDetailsUtils;

@Service
public class ProductMediaServiceImpl implements ProductMediaService, AttachmentService {

    private final ProductMediaRepository productMediaRepository;
    private final FileStorageService storageService;
    private final ProductRepository productRepository;

    @Value("${storage.uploads.path.product.media}")
    private String productMediaStoragePath;
    private ModelMapper modelMapper;

    public ProductMediaServiceImpl(ProductMediaRepository productMediaRepository, FileStorageService storageService,
            ProductRepository productRepository) {
        this.productMediaRepository = productMediaRepository;
        this.storageService = storageService;
        this.productRepository = productRepository;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') || hasRole('VERIFIED_VENDOR')")
    @Transactional
    public void addProductMedia(UUID productId, AttachmentInfo attachmentInfo) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        UUID authenticatedUserId = UserDetailsUtils.getAuthenticatedUser().getId();
        if (!UserDetailsUtils.hasRole(Role.ADMIN)
                && !isProductOwner(product, authenticatedUserId))
            throw new UnauthorizedAccessException(authenticatedUserId.toString(), "upload", "a product media");

        ProductMedia productMedia = new ProductMedia(attachmentInfo.getFilename(), attachmentInfo.getSize(),
                attachmentInfo.getPath());
        saveProductMedia(product, productMedia);
    }

    private void saveProductMedia(Product product, ProductMedia productMedia) {
        product.addProductMedia(productMedia);
        productRepository.save(product);
    }

    private boolean isProductOwner(Product product, UUID userId) {
        return product.getVendor().getId().equals(userId);
    }

    private boolean isValidExtension(String contentType) {
        return contentType.equalsIgnoreCase("png") || contentType.equalsIgnoreCase("jpg")
                || contentType.equalsIgnoreCase("jpeg") || contentType.equalsIgnoreCase("jfif");
    }

    @Override
    @Transactional
    public void deleteProductMedia(UUID id) {
        ProductMedia productMedia = productMediaRepository.findById(id).orElse(null);
        if (productMedia == null)
            return;

        storageService.deleteFile(productMedia.getPath());
        productMediaRepository.delete(productMedia);
    }

    @Override
    @Transactional
    public void updateProductMedia(UUID id, AttachmentInfo attachmentInfo) {
        ProductMedia productMedia = new ProductMedia(attachmentInfo.getFilename(), attachmentInfo.getSize(),
                attachmentInfo.getPath());
        if (!productMediaRepository.existsById(id))
            throw new ProductMediaNotFoundException();

        productMediaRepository.save(productMedia);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') || hasRole('VERIFIED_VENDOR')")
    public AttachmentInfo saveAttachment(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (fileExtension == null || !isValidExtension(fileExtension))
            throw new UnsupportedFileExtensionException("Unsupported file extension, provided value: " + fileExtension);

        String filename = UUID.randomUUID().toString() + "." + fileExtension;
        String filePath = productMediaStoragePath + File.separator + filename;
        return storageService.save(file, filePath);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttachmentInfo> getProductMediaByProductId(UUID productId) {
        Product product = productRepository.getReferenceById(productId);
        List<ProductMedia> productMedia = productMediaRepository.findAllByProduct(product);
        return CollectionUtils.isEmpty(productMedia) ? Collections.emptyList()
                : productMedia.stream().map(media -> modelMapper.map(media, AttachmentInfo.class)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AttachmentInfo getProductMedia(UUID productMediaId) {
        ProductMedia productMedia = productMediaRepository.findById(productMediaId)
                .orElseThrow(ProductMediaNotFoundException::new);
        return modelMapper.map(productMedia, AttachmentInfo.class);
    }
}
