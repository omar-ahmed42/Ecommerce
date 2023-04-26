package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;

public class WishlistPK implements Serializable {
    @Column(name = "customer_id", nullable = false, insertable = false, updatable = false)
    @Id
    private UUID customerId;
    @Column(name = "product_id", nullable = false, insertable = false, updatable = false)
    @Id
    private UUID productId;

    public WishlistPK(UUID customerId, UUID productId) {
        this.customerId = customerId;
        this.productId = productId;
    }

    public WishlistPK() {
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WishlistPK that = (WishlistPK) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        int result = customerId.hashCode();
        result = 31 * result + productId.hashCode();
        return result;
    }
}
