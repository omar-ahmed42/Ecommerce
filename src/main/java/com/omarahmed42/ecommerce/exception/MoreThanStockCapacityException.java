package com.omarahmed42.ecommerce.exception;

public class MoreThanStockCapacityException extends RuntimeException {
    public MoreThanStockCapacityException(String message) {
        super(message);
    }
}
