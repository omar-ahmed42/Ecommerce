package com.omarahmed42.ecommerce.exception;

public class BadUsernameException extends RuntimeException {
    public static final String BAD_USERNAME = "Incorrect email or password";

    public BadUsernameException() {
        super(BAD_USERNAME);
    }

    public BadUsernameException(String msg) {
        super(msg);
    }
}
