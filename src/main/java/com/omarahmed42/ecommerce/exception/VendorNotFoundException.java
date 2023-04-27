package com.omarahmed42.ecommerce.exception;

public class VendorNotFoundException extends NotFoundException {
    private static final String VENDOR_NOT_FOUND = "Vendor not found";

    public VendorNotFoundException() {
        super(VENDOR_NOT_FOUND);
    }

    public VendorNotFoundException(String message) {
        super(message);
    }
}
