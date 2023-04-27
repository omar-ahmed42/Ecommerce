package com.omarahmed42.ecommerce.exception;

public class CustomerOrderNotFoundException extends NotFoundException {
    private static final String CUSTOMER_ORDER_NOT_FOUND = "Customer Order not found";

    public CustomerOrderNotFoundException() {
        super(CUSTOMER_ORDER_NOT_FOUND);
    }

    public CustomerOrderNotFoundException(String message) {
        super(message);
    }
}
