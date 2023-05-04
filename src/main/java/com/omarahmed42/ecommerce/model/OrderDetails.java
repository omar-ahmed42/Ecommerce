package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.omarahmed42.ecommerce.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Audited
public class OrderDetails implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, columnDefinition = "BINARY(16)")
    @Access(AccessType.PROPERTY)
    private UUID id;

    @Basic
    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Basic
    @Column(name = "purchase_date", nullable = false)
    private Instant purchaseDate;

    @Basic
    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private Instant createdAt;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @OneToOne(mappedBy = "orderDetails", fetch = FetchType.LAZY)
    private CustomerOrders customerOrder;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderDetails", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id", referencedColumnName = "id", columnDefinition = "BINARY(16)")
    private BillingAddress billingAddress;

    public void addAllOrderItems(Collection<OrderItem> orderItems) {
        if (orderItems.isEmpty())
            return;
        for (OrderItem orderItem : orderItems) {
            addOrderItem(orderItem);
        }
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrderDetails(this);
    }

    public void removeComment(OrderItem orderItem) {
        orderItem.setOrderDetails(null);
        this.orderItems.remove(orderItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderDetails orders = (OrderDetails) o;
        return Objects.equals(id, orders.id) && Objects.equals(totalPrice, orders.totalPrice)
                && Objects.equals(purchaseDate, orders.purchaseDate) && Objects.equals(createdAt, orders.createdAt)
                && Objects.equals(status, orders.status);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(totalPrice, purchaseDate, createdAt, status);
        result = 31 * result + id.hashCode();
        return result;
    }
}
