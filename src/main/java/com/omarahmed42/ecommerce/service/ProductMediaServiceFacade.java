package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.omarahmed42.ecommerce.DTO.AttachmentInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductMediaServiceFacade {
    private final ProductMediaService productMediaService;
    private final AttachmentService attachmentService;

    @PreAuthorize("hasRole('ADMIN') || hasRole('VERIFIED_VENDOR')")
    public void addProductMedia(UUID productId, MultipartFile file) {
        AttachmentInfo attachmentInfo = attachmentService.saveAttachment(file);
        productMediaService.addProductMedia(productId, attachmentInfo);
    }
}
