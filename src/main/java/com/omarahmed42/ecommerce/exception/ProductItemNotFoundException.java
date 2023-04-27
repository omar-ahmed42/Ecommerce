package com.omarahmed42.ecommerce.exception;

public class ProductItemNotFoundException extends NotFoundException {
    private static final String PRODUCT_ITEM_NOT_FOUND = "Product item not found";

    public ProductItemNotFoundException() {
        super(PRODUCT_ITEM_NOT_FOUND);
    }

    public ProductItemNotFoundException(String message) {
        super(message);
    }
}
