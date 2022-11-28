package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.ProductMedia;

import java.util.List;

public interface ProductMediaService {
    void addProductMedia(ProductMedia productMedia);
    void addProductMedia(List<ProductMedia> productMedia);
    void deleteProductMedia(ProductMedia productMedia);
    void deleteProductMedia(List<ProductMedia> productMedia);
    void deleteProductMediaByProductId(byte[] productId);
    void updateProductMedia(ProductMedia productMedia);
}
