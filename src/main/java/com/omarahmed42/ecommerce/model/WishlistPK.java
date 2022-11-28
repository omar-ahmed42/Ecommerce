package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Id;

public class WishlistPK implements Serializable {
    @Column(name = "customer_id", nullable = false, insertable = false, updatable = false)
    @Id
    private byte[] customerId;
    @Column(name = "product_id", nullable = false, insertable = false, updatable = false)
    @Id
    private byte[] productId;

    public WishlistPK(byte[] customerId, byte[] productId) {
        this.customerId = customerId;
        this.productId = productId;
    }

    public WishlistPK() {
    }

    public byte[] getCustomerId() {
        return customerId;
    }

    public void setCustomerId(byte[] customerId) {
        this.customerId = customerId;
    }

    public byte[] getProductId() {
        return productId;
    }

    public void setProductId(byte[] productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WishlistPK that = (WishlistPK) o;
        return Arrays.equals(customerId, that.customerId) && Arrays.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(customerId);
        result = 31 * result + Arrays.hashCode(productId);
        return result;
    }
}
