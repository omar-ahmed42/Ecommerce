package com.omarahmed42.ecommerce.exception;

public class BannedUserNotFoundException extends NotFoundException {
    private static final String BANNED_USER_NOT_FOUND = "Banned user not found";

    public BannedUserNotFoundException() {
        super(BANNED_USER_NOT_FOUND);
    }

    public BannedUserNotFoundException(String message) {
        super(message);
    }
}
