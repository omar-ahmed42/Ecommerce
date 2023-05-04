package com.omarahmed42.ecommerce.exception;

public class OrderItemNotFoundException extends NotFoundException {
    private static final String PRODUCT_ITEM_NOT_FOUND = "Order item not found";

    public OrderItemNotFoundException() {
        super(PRODUCT_ITEM_NOT_FOUND);
    }

    public OrderItemNotFoundException(String message) {
        super(message);
    }
}
