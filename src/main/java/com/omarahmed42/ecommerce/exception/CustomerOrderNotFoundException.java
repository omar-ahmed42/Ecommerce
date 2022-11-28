package com.omarahmed42.ecommerce.exception;

public class CustomerOrderNotFoundException extends RuntimeException {
    public CustomerOrderNotFoundException(String message) {
        super(message);
    }
}
