package com.omarahmed42.ecommerce.enums;

public enum Role {
    CUSTOMER("CUSTOMER", 1), VERIFIED_VENDOR("VERIFIED_VENDOR", 2), UNVERIFIED_VENDOR("UNVERIFIED_VENDOR", 3), ADMIN("ADMIN", 4);

    private final String text;
    private final int value;

    Role(final String text, final int value){
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
