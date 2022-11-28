package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_item", schema = "ecommerce")
public class ProductItem implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false, updatable = false)
    private byte[] id;

    @Basic
    @Column(name = "order_id", nullable = false)
    private byte[] orderId;

    @Basic
    @Column(name = "product_id", nullable = false)
    private byte[] productId;

    @Basic
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Basic
    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product productByProductId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id", updatable = false, insertable = false)
    private Orders ordersById;

    public ProductItem() {
    }

    public ProductItem(byte[] id) {
        this.id = id;
    }

    public ProductItem(byte[] productId, BigDecimal totalPrice, int quantity) {
        this.productId = productId;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductItem that = (ProductItem) o;
        return quantity == that.quantity && Arrays.equals(id, that.id) && Arrays.equals(productId, that.productId)
                && Objects.equals(totalPrice, that.totalPrice);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(quantity, totalPrice);
        result = 31 * result + Arrays.hashCode(id);
        result = 31 * result + Arrays.hashCode(productId);
        return result;
    }
}
