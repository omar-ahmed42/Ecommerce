package com.omarahmed42.ecommerce.enums;

public enum Status {
    FAILED("FAILED", 1), COMPLETED("COMPLETED", 2), PENDING("PENDING", 3), CANCELLED("CANCELLED", 4);

    private final String text;
    private final int value;

    Status(final String text, final int value){
        this.text = text;
        this.value = value;
    }

    @Override
    public String toString(){
        return text;
    }

    public int getValue(){
        return value;
    }
}