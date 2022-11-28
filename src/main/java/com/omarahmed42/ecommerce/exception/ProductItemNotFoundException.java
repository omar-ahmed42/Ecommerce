package com.omarahmed42.ecommerce.exception;

public class ProductItemNotFoundException extends RuntimeException {
    public ProductItemNotFoundException(String message) {
        super(message);
    }
}
