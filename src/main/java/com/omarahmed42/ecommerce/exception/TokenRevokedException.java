package com.omarahmed42.ecommerce.exception;

public class TokenRevokedException extends RuntimeException {

    public TokenRevokedException() {
        super("Token has been revoked");
    }

    public TokenRevokedException(String message) {
        super(message);
    }

}
