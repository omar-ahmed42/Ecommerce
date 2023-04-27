package com.omarahmed42.ecommerce.exception;

public class PaymentNotFoundException extends NotFoundException {
    private static final String PAYMENT_NOT_FOUND = "Payment not found";

    public PaymentNotFoundException() {
        super(PAYMENT_NOT_FOUND);
    }

    public PaymentNotFoundException(String message) {
        super(message);
    }
}
