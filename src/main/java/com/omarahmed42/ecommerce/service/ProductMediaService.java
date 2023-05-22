package com.omarahmed42.ecommerce.service;

import java.util.List;
import java.util.UUID;

import com.omarahmed42.ecommerce.DTO.AttachmentInfo;

public interface ProductMediaService {
    void addProductMedia(UUID productId, AttachmentInfo attachmentInfo);

    void deleteProductMedia(UUID id);

    void updateProductMedia(UUID id, AttachmentInfo attachmentInfo);

    List<AttachmentInfo> getProductMediaByProductId(UUID productId);

    AttachmentInfo getProductMedia(UUID productMediaId);
}
