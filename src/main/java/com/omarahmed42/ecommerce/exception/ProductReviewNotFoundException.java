package com.omarahmed42.ecommerce.exception;

public class ProductReviewNotFoundException extends NotFoundException {
    private static final String PRODUCT_REVIEW_NOT_FOUND = "Product review not found";

    public ProductReviewNotFoundException() {
        super(PRODUCT_REVIEW_NOT_FOUND);
    }

    public ProductReviewNotFoundException(String message) {
        super(message);
    }
}
