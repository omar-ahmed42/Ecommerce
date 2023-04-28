package com.omarahmed42.ecommerce.exception;

public class TokenExpiredException extends RuntimeException {
    public static final int STATUS_CODE = 400;

    public TokenExpiredException() {
        super("Token has expired");
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
