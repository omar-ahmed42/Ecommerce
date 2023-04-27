package com.omarahmed42.ecommerce.exception;

public class ProductNotFoundException extends NotFoundException {
    private static final String PRODUCT_NOT_FOUND = "Product not found";

    public ProductNotFoundException() {
        super(PRODUCT_NOT_FOUND);
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
