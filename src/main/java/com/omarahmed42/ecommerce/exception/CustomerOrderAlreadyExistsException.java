package com.omarahmed42.ecommerce.exception;

public class CustomerOrderAlreadyExistsException extends RuntimeException {
    public CustomerOrderAlreadyExistsException(String message) {
        super(message);
    }
}
