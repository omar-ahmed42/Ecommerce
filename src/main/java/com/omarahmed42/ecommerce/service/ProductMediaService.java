package com.omarahmed42.ecommerce.service;

import java.util.List;
import java.util.UUID;

import com.omarahmed42.ecommerce.model.ProductMedia;

public interface ProductMediaService {
    void addProductMedia(ProductMedia productMedia);
    void addProductMedia(List<ProductMedia> productMedia);
    void deleteProductMedia(ProductMedia productMedia);
    void deleteProductMedia(List<ProductMedia> productMedia);
    void deleteProductMediaByProductId(UUID productId);
    void updateProductMedia(ProductMedia productMedia);
}
