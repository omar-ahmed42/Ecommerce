package com.omarahmed42.ecommerce.exception;

public class WishlistNotFoundException extends NotFoundException {
    private static final String WISHLIST_NOT_FOUND = "Wishlist not found";

    public WishlistNotFoundException() {
        super(WISHLIST_NOT_FOUND);
    }

    public WishlistNotFoundException(String message) {
        super(message);
    }
}
