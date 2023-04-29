package com.omarahmed42.ecommerce.enums;

public enum TokenStatus {
    CONSUMED("CONSUMED", 1), REVOKED("REVOKED", 2), EXPIRED("EXPIRED", 3), VALID("VALID", 4);

    private final String text;
    private final int value;

    TokenStatus(final String text, final int value){
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
