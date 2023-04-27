package com.omarahmed42.ecommerce.exception;

public class NotFoundException extends RuntimeException {
    public static final int STATUS_CODE = 404;

    public NotFoundException() {
        super("Resource not found");
    }

    public NotFoundException(String msg) {
        super(msg);
    }
}
