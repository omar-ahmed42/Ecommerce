package com.omarahmed42.ecommerce.exception;

public class VerificationTokenNotFoundException extends NotFoundException {
    private static final String VERIFICATION_TOKEN_NOT_FOUND = "Verification token not found";

    public VerificationTokenNotFoundException() {
        super(VERIFICATION_TOKEN_NOT_FOUND);
    }

    public VerificationTokenNotFoundException(String message) {
        super(message);
    }
}
