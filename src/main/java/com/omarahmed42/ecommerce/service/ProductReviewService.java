package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.model.ProductReview;

public interface ProductReviewService {
    void addProductReview(ProductReview productReview);
    void deleteProductReview(UUID id);
    void updateProductReview(ProductReview productReview);
    UUID findCustomerIdById(UUID productReviewId);
}
