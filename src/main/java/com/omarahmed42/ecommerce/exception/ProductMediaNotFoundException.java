package com.omarahmed42.ecommerce.exception;

public class ProductMediaNotFoundException extends NotFoundException{
    private static final String PRODUCT_MEDIA_NOT_FOUND = "Product Media not found";

    public ProductMediaNotFoundException() {
        super(PRODUCT_MEDIA_NOT_FOUND);
    }

    public ProductMediaNotFoundException(String message) {
        super(message);
    }
}
