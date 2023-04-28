package com.omarahmed42.ecommerce.exception;

public class TokenAlreadyConsumedException extends RuntimeException {
    public static final int STATUS_CODE = 400;

    public TokenAlreadyConsumedException() {
        super("Token already consumed");
    }

    public TokenAlreadyConsumedException(String message) {
        super(message);
    }
}
