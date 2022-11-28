package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(WishlistPK.class)
public class Wishlist implements Serializable {
    @Id
    @Column(name = "customer_id", nullable = false, insertable = false, updatable = false)
    private byte[] customerId;

    @Id
    @Column(name = "product_id", nullable = false, insertable = false, updatable = false)
    private byte[] productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id", nullable = true, insertable = false, updatable = false)
    private Customer customerByCustomerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = true, insertable = false, updatable = false)
    private Product productByProductId;

    public Wishlist() {
    }

    public Wishlist(byte[] customerId, byte[] productId) {
        this.customerId = customerId;
        this.productId = productId;
    }

    public byte[] getCustomerId() {
        return customerId;
    }

    public void setCustomerId(byte[] customerId) {
        this.customerId = customerId;
    }

    public byte[] getProductId() {
        return productId;
    }

    public void setProductId(byte[] productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Wishlist wishlist = (Wishlist) o;
        return Arrays.equals(customerId, wishlist.customerId) && Arrays.equals(productId, wishlist.productId);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(customerId);
        result = 31 * result + Arrays.hashCode(productId);
        return result;
    }

    public Customer getCustomerByCustomerId() {
        return customerByCustomerId;
    }

    public void setCustomerByCustomerId(Customer customerByCustomerId) {
        this.customerByCustomerId = customerByCustomerId;
    }

    public Product getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(Product productByProductId) {
        this.productByProductId = productByProductId;
    }
}
