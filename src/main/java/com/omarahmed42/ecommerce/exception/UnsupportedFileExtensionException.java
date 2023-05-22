package com.omarahmed42.ecommerce.exception;

public class UnsupportedFileExtensionException extends InvalidInputException {
    private static final String UNSUPPORTED_FILE_EXTENSION = "Unsupported file extension";

    public UnsupportedFileExtensionException(String message) {
        super(message);
    }

    public UnsupportedFileExtensionException() {
        super(UNSUPPORTED_FILE_EXTENSION);
    }
}
