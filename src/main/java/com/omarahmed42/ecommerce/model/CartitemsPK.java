package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;

public class CartitemsPK implements Serializable {
    @Column(name = "customer_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    @Id
    private UUID customerId;

    @Column(name = "product_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    @Id
    private UUID productId;

    public CartitemsPK() {
    }

    public CartitemsPK(UUID customerId, UUID productItemId) {
        this.customerId = customerId;
        this.productId = productItemId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getProductItemId() {
        return productId;
    }

    public void setProductItemId(UUID productItemId) {
        this.productId = productItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartitemsPK that = (CartitemsPK) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        int result = customerId.hashCode();
        result = 31 * result + productId.hashCode();
        return result;
    }
}
