package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(CartitemsPK.class)
@EntityListeners(AuditingEntityListener.class)
public class Cartitems implements Serializable {
    @Id
    @Column(name = "customer_id", nullable = false, insertable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID customerId;

    @Id
    @Column(name = "product_id", nullable = false, insertable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID productId;

    @Basic
    @Column(name = "quantity")
    private int quantity;

    @Basic
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Basic
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Basic
    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private Instant createdAt;

    @Basic
    @Column(name = "modified_at")
    @LastModifiedDate
    private Instant modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id", insertable = false, updatable = false, nullable = false)
    private Customer customerByCustomerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Product productByProductId;

    public Cartitems() {
    }

    public Cartitems(UUID customerId, UUID productId) {
        this.customerId = customerId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Cartitems cartitems = (Cartitems) o;
        return Objects.equals(customerId, cartitems.customerId) && Objects.equals(productId, cartitems.productId);
    }

    @Override
    public int hashCode() {
        int result = customerId.hashCode();
        result = 31 * result + productId.hashCode();
        return result;
    }

    @PrePersist
    @PreUpdate
    private void calculateSubtotal() {
        this.subtotal = this.price.multiply(BigDecimal.valueOf(quantity));
    }
}
