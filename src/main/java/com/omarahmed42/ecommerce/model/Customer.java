package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer implements Serializable {
    @Id
    @Basic
    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userId;

    @Basic
    @Column(name = "billing_address_id", nullable = true, insertable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID billingAddressId;

    @OneToMany(mappedBy = "customerByCustomerId", fetch = FetchType.LAZY)
    private Collection<Cartitems> cartitemsById;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private User userByUserId;

    public Customer() {
    }

    public Customer(UUID userId) {
        this.userId = userId;
    }

    // TODO: Make the relationship OneToMany instead of ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id", referencedColumnName = "id", nullable = true)
    private BillingAddress billingAddressByBillingAddressId;

    @OneToMany(mappedBy = "customerByCustomerId", fetch = FetchType.LAZY)
    private Collection<CustomerOrders> customerOrdersById;

    @OneToMany(mappedBy = "customerByCustomerId", fetch = FetchType.LAZY)
    private Collection<ProductReview> productReviewsById;

    @OneToMany(mappedBy = "customerByCustomerId", fetch = FetchType.LAZY)
    private Collection<Wishlist> wishlistsById;

    public UUID getId() {
        return this.userId;
    }

    public void setId(UUID id){
        this.userId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return userId.equals(customer.userId) && billingAddressId.equals(customer.billingAddressId);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + userId.hashCode();
        result = 31 * result + billingAddressId.hashCode();
        return result;
    }
}
