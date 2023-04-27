package com.omarahmed42.ecommerce.exception;

public class CustomerNotFoundException extends NotFoundException {
    private static final String CUSTOMER_NOT_FOUND = "Customer not found";

    public CustomerNotFoundException() {
        super(CUSTOMER_NOT_FOUND);
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
