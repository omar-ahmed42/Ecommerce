package com.omarahmed42.ecommerce.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public static final int STATUS_CODE = 403;
    private static final String UNAUTHORIZED_ACCESS = "Unauthorized access to resource";

    public UnauthorizedAccessException() {
        super(UNAUTHORIZED_ACCESS);
    }

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String userId, String action, String resource) {
        super("Unauthorized user with id " + userId + " tried to " + action + " " + resource);
    }

}
