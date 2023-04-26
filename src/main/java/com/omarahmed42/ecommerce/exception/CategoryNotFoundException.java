package com.omarahmed42.ecommerce.exception;

public class CategoryNotFoundException extends RuntimeException {
    private static final String CATEGORY_NOT_FOUND = "Category not found";

    public CategoryNotFoundException() {
        super(CATEGORY_NOT_FOUND);
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
