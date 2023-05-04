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

import org.hibernate.envers.Audited;

@Entity
@IdClass(WishlistPK.class)
@Audited
public class Wishlist implements Serializable {
    @Id
    @Column(name = "customer_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID customerId;

    @Id
    @Column(name = "product_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

    public Wishlist() {
    }

    public Wishlist(UUID customerId, UUID productId) {
        this.customerId = customerId;
        this.productId = productId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Wishlist wishlist = (Wishlist) o;
        return Objects.equals(customerId, wishlist.customerId) && Objects.equals(productId, wishlist.productId);
    }

    @Override
    public int hashCode() {
        int result = customerId.hashCode();
        result = 31 * result + productId.hashCode();
        return result;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customerByCustomerId) {
        this.customer = customerByCustomerId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product productByProductId) {
        this.product = productByProductId;
    }
}
