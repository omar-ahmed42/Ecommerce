package com.omarahmed42.ecommerce.exception;

public class MissingFieldException extends ValidationException {
    public MissingFieldException(String message) {
        super(message);
    }
}
