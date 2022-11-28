package com.omarahmed42.ecommerce.exception;

public class BannedUserNotFoundException extends RuntimeException {
    public BannedUserNotFoundException(String message) {
        super(message);
    }
}
