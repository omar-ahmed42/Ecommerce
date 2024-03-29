package com.omarahmed42.ecommerce.exception;

public class UserNotFoundException extends NotFoundException {
    private static final String USER_NOT_FOUND = "User not found";

    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
