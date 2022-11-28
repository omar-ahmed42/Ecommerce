package com.omarahmed42.ecommerce.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Arrays;

@Entity
@Table(name = "customer_orders", schema = "ecommerce")
@IdClass(CustomerOrdersPK.class)
@Getter
@Setter
public class CustomerOrders implements Serializable{
    @Id
    @Column(name = "customer_id", nullable = false, updatable = false, insertable = false)
    private byte[] customerId;

    @Id
    @Column(name = "order_id", nullable = false, updatable = false, insertable = false)
    private byte[] orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id", nullable = false, insertable = false, updatable = false)
    private Customer customerByCustomerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Orders ordersByOrderId;

    public CustomerOrders() {
    }

    public CustomerOrders(byte[] customerId, byte[] orderId) {
        this.customerId = customerId;
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerOrders that = (CustomerOrders) o;
        return Arrays.equals(customerId, that.customerId) && Arrays.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(customerId);
        result = 31 * result + Arrays.hashCode(orderId);
        return result;
    }
}
