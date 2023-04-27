package com.omarahmed42.ecommerce.exception;

public class OrderNotFoundException extends RuntimeException {
    private static final String ORDER_NOT_FOUND = "Order not found";

    public OrderNotFoundException() {
        super(ORDER_NOT_FOUND);
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
