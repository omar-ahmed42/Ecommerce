package com.omarahmed42.ecommerce.exception;

public class BillingAddressNotFoundException extends RuntimeException {
    private static final String BILLING_ADDRESS_NOT_FOUND = "Billing address not found";

    public BillingAddressNotFoundException() {
        super(BILLING_ADDRESS_NOT_FOUND);
    }

    public BillingAddressNotFoundException(String message) {
        super(message);
    }
}
