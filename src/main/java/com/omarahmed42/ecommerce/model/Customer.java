package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.envers.Audited;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Audited
public class Customer implements Serializable {
    @Id
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    @Access(AccessType.PROPERTY)
    private UUID id;

    @Basic
    @Column(name = "billing_address_id", nullable = true, insertable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID billingAddressId;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Collection<CartItem> cartItems;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id", columnDefinition = "BINARY(16)")
    private User user;

    public Customer() {
    }

    public Customer(UUID userId) {
        this.id = userId;
    }

    // TODO: Make the relationship OneToMany instead of ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id", referencedColumnName = "id", nullable = true)
    private BillingAddress billingAddressByBillingAddressId;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Collection<ProductReview> productReviews;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Collection<Wishlist> wishlists;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Customer customer = (Customer) o;
        return id.equals(customer.id) && billingAddressId.equals(customer.billingAddressId);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + billingAddressId.hashCode();
        return result;
    }
}
