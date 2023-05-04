package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Audited
public class OrderItem implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    @Access(AccessType.PROPERTY)
    private UUID id;

    @Basic
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Basic
    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", columnDefinition = "BINARY(16)")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id", columnDefinition = "BINARY(16)")
    private OrderDetails orderDetails;

    public OrderItem() {
    }

    public OrderItem(UUID id) {
        this.id = id;
    }

    public OrderItem(Product product, BigDecimal totalPrice, int quantity) {
        this.product = product;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + quantity;
        result = prime * result + ((totalPrice == null) ? 0 : totalPrice.hashCode());
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + ((orderDetails == null) ? 0 : orderDetails.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrderItem other = (OrderItem) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (quantity != other.quantity)
            return false;
        if (totalPrice == null) {
            if (other.totalPrice != null)
                return false;
        } else if (!totalPrice.equals(other.totalPrice))
            return false;
        if (product == null) {
            if (other.product != null)
                return false;
        } else if (!product.equals(other.product))
            return false;
        if (orderDetails == null) {
            if (other.orderDetails != null)
                return false;
        } else if (!orderDetails.equals(other.orderDetails))
            return false;
        return true;
    }
}
