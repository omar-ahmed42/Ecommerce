package com.omarahmed42.ecommerce.exception;

public class CartItemAlreadyExistsException extends AlreadyExistsException {
    private static final String CART_ITEM_ALREADY_EXISTS = "Cart item already exists";

    public CartItemAlreadyExistsException() {
        super(CART_ITEM_ALREADY_EXISTS);
    }

    public CartItemAlreadyExistsException(String msg) {
        super(msg);
    }
}
