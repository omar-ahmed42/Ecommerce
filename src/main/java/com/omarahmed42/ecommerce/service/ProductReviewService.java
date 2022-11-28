package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.ProductReview;

public interface ProductReviewService {
    void addProductReview(ProductReview productReview);
    void deleteProductReview(byte[] id);
    void updateProductReview(ProductReview productReview);
    byte[] findCustomerIdById(byte[] productReviewId);
}
