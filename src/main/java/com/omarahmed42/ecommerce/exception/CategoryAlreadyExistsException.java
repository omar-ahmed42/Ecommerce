package com.omarahmed42.ecommerce.exception;

public class CategoryAlreadyExistsException extends AlreadyExistsException {
    private static final String CATEGORY_ALREADY_EXISTS = "Category already exists";

    public CategoryAlreadyExistsException() {
        super(CATEGORY_ALREADY_EXISTS);
    }

    public CategoryAlreadyExistsException(String msg) {
        super(msg);
    }
}
