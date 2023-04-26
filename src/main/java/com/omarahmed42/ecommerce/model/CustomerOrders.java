package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@IdClass(CustomerOrdersPK.class)
@Getter
@Setter
public class CustomerOrders implements Serializable{
    @Id
    @Column(name = "customer_id", nullable = false, updatable = false, insertable = false, columnDefinition = "BINARY(16)")
    private UUID customerId;

    @Id
    @Column(name = "order_id", nullable = false, updatable = false, insertable = false, columnDefinition = "BINARY(16)")
    private UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id", nullable = false, insertable = false, updatable = false)
    private Customer customerByCustomerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Orders ordersByOrderId;

    public CustomerOrders() {
    }

    public CustomerOrders(UUID customerId, UUID orderId) {
        this.customerId = customerId;
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerOrders that = (CustomerOrders) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        int result = customerId.hashCode();
        result = 31 * result + orderId.hashCode();
        return result;
    }
}
