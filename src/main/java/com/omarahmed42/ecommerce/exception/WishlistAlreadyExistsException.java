package com.omarahmed42.ecommerce.exception;

public class WishlistAlreadyExistsException extends AlreadyExistsException {
    private static final String WISHLIST_ALREADY_EXISTS = "Wishlist item already exists";

    public WishlistAlreadyExistsException() {
        super(WISHLIST_ALREADY_EXISTS);
    }

    public WishlistAlreadyExistsException(String message) {
        super(message);
    }
}
