package com.omarahmed42.ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

import com.omarahmed42.ecommerce.DTO.AttachmentInfo;

public interface AttachmentService {
    AttachmentInfo saveAttachment(MultipartFile file);
}
