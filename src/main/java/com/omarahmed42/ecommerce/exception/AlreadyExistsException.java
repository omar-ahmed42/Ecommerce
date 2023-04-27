package com.omarahmed42.ecommerce.exception;

public class AlreadyExistsException extends RuntimeException {
    public static final int STATUS_CODE = 409;

    public AlreadyExistsException() {
        super("Resource already exists");
    }

    public AlreadyExistsException(String msg) {
        super(msg);
    }

}
