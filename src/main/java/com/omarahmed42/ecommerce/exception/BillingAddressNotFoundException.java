package com.omarahmed42.ecommerce.exception;

public class BillingAddressNotFoundException extends RuntimeException {
    public BillingAddressNotFoundException(String message) {
        super(message);
    }
}
