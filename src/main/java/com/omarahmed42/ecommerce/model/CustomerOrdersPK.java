package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Id;

public class CustomerOrdersPK implements Serializable {
    @Column(name = "customer_id", nullable = false, insertable = false, updatable = false)
    @Id
    private byte[] customerId;
    @Column(name = "order_id", nullable = false, insertable = false, updatable = false)
    @Id
    private byte[] orderId;

    public CustomerOrdersPK(byte[] customerId, byte[] orderId) {
        this.customerId = customerId;
        this.orderId = orderId;
    }

    public CustomerOrdersPK() {
    }

    public byte[] getCustomerId() {
        return customerId;
    }

    public void setCustomerId(byte[] customerId) {
        this.customerId = customerId;
    }

    public byte[] getOrderId() {
        return orderId;
    }

    public void setOrderId(byte[] orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CustomerOrdersPK that = (CustomerOrdersPK) o;
        return Arrays.equals(customerId, that.customerId) && Arrays.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(customerId);
        result = 31 * result + Arrays.hashCode(orderId);
        return result;
    }
}
