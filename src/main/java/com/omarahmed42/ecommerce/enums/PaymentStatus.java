package com.omarahmed42.ecommerce.enums;

public enum PaymentStatus {
    FAILED("FAILED", 1), COMPLETED("COMPLETED", 2), CREATED("CREATED", 3), PENDING("PENDING", 4), CANCELLED("CANCELLED", 5);

    private final String text;
    private final int value;

    PaymentStatus(final String text, final int value) {
        this.text = text;
        this.value = value;
    }

    @Override
    public String toString() {
        return text;
    }

    public int getValue() {
        return value;
    }
}
