package com.omarahmed42.ecommerce.exception;

public class CustomerOrderAlreadyExistsException extends AlreadyExistsException {
    private static final String CUSTOMER_ORDER_ALREADY_EXISTS = "Customer order already exists";

    public CustomerOrderAlreadyExistsException() {
        super(CUSTOMER_ORDER_ALREADY_EXISTS);
    }

    public CustomerOrderAlreadyExistsException(String msg) {
        super(msg);
    }
}
