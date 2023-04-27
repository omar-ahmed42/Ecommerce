package com.omarahmed42.ecommerce.exception;

public class ValidationException extends RuntimeException {
    public static final int STATUS_CODE = 422;

    public ValidationException(String message) {
        super(message);
    }
}
