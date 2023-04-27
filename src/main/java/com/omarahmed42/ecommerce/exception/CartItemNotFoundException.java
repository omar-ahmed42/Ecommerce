package com.omarahmed42.ecommerce.exception;

public class CartItemNotFoundException extends NotFoundException{
    private static final String CART_ITEM_NOT_FOUND = "Cart item not found";

    public CartItemNotFoundException() {
        super(CART_ITEM_NOT_FOUND);
    }

    public CartItemNotFoundException(String message) {
        super(message);
    }
}
