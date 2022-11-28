package com.omarahmed42.ecommerce.exception;

public class CategoryProductNotFoundException extends RuntimeException {
    public CategoryProductNotFoundException(String message) {
        super(message);
    }
}
