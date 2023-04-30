package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.DTO.ProductReviewRequest;

public interface ProductReviewService {
    void addProductReview(UUID productId, ProductReviewRequest productReviewRequest);

    void deleteProductReview(UUID id);

    void updateProductReview(UUID id, ProductReviewRequest productReviewRequest);
}
