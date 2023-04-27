package com.omarahmed42.ecommerce.exception;

public class EmailAlreadyExistsException extends AlreadyExistsException {
    private static final String EMAIL_ALREADY_ALREADY_EXISTS = "Email already exists";

    public EmailAlreadyExistsException() {
        super(EMAIL_ALREADY_ALREADY_EXISTS);
    }

    public EmailAlreadyExistsException(String msg) {
        super(msg);
    }
}
