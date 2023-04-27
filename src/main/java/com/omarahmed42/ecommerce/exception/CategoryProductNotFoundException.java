package com.omarahmed42.ecommerce.exception;

public class CategoryProductNotFoundException extends RuntimeException {
    private static final String CATEGORY_PRODUCT_NOT_FOUND = "Category product not found";

    public CategoryProductNotFoundException() {
        super(CATEGORY_PRODUCT_NOT_FOUND);
    }

    public CategoryProductNotFoundException(String message) {
        super(message);
    }
}
