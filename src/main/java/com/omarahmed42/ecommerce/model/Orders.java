package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
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

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.omarahmed42.ecommerce.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Orders implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false, updatable = false)
    private byte[] id;
    @Basic
    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Basic
    @Column(name = "purchase_date", nullable = false)
    private Instant purchaseDate;

    @Basic
    @Column(name = "billing_address_id", nullable = false)
    private byte[] billingAddressId;

    @Basic
    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private Instant createdAt;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @OneToOne(mappedBy = "ordersByOrderId", fetch = FetchType.LAZY)
    private CustomerOrders customerOrdersById;

    @OneToOne(mappedBy = "orderByOrderId", fetch = FetchType.LAZY)
    private Payment payment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ordersById")
    private List<ProductItem> productItemsById;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id", referencedColumnName = "id", insertable = false, updatable = false)
    private BillingAddress billingAddressByBillingAddressId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Orders orders = (Orders) o;
        return Arrays.equals(id, orders.id) && Objects.equals(totalPrice, orders.totalPrice)
                && Objects.equals(purchaseDate, orders.purchaseDate) && Objects.equals(createdAt, orders.createdAt)
                && Objects.equals(status, orders.status);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(totalPrice, purchaseDate, createdAt, status);
        result = 31 * result + Arrays.hashCode(id);
        return result;
    }
}
