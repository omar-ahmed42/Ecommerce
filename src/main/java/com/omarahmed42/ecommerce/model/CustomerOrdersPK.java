package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;

public class CustomerOrdersPK implements Serializable {
    @Column(name = "customer_id", updatable = false, columnDefinition = "BINARY(16)")
    @Id
    private UUID customerId;
    @Column(name = "order_id", updatable = false, columnDefinition = "BINARY(16)")
    @Id
    private UUID orderId;

    public CustomerOrdersPK(UUID customerId, UUID orderId) {
        this.customerId = customerId;
        this.orderId = orderId;
    }

    public CustomerOrdersPK() {
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CustomerOrdersPK that = (CustomerOrdersPK) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        int result = customerId.hashCode();
        result = 31 * result + orderId.hashCode();
        return result;
    }
}
