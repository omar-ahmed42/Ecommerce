package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Id;

public class CartitemsPK implements Serializable {
    @Column(name = "customer_id", nullable = false, insertable = false, updatable = false)
    @Id
    private byte[] customerId;

    @Column(name = "product_id", nullable = false, insertable = false, updatable = false)
    @Id
    private byte[] productId;

    public CartitemsPK() {
    }

    public CartitemsPK(byte[] customerId, byte[] productItemId) {
        this.customerId = customerId;
        this.productId = productItemId;
    }

    public byte[] getCustomerId() {
        return customerId;
    }

    public void setCustomerId(byte[] customerId) {
        this.customerId = customerId;
    }

    public byte[] getProductItemId() {
        return productId;
    }

    public void setProductItemId(byte[] productItemId) {
        this.productId = productItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartitemsPK that = (CartitemsPK) o;
        return Arrays.equals(customerId, that.customerId) && Arrays.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(customerId);
        result = 31 * result + Arrays.hashCode(productId);
        return result;
    }
}
