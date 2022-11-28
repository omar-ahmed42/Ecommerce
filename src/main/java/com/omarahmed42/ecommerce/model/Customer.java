package com.omarahmed42.ecommerce.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Getter
@Setter
public class Customer implements Serializable {
    @Id
    @Basic
    @Column(name = "user_id", nullable = false)
    private byte[] userId;

    @Basic
    @Column(name = "billing_address_id", nullable = true, insertable = false, updatable = false)
    private byte[] billingAddressId;

    @OneToMany(mappedBy = "customerByCustomerId", fetch = FetchType.LAZY)
    private Collection<Cartitems> cartitemsById;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private User userByUserId;

    public Customer() {
    }

    public Customer(byte[] userId) {
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

    public byte[] getId() {
        return this.userId;
    }

    public void setId(byte[] id){
        this.userId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Arrays.equals(userId, customer.userId) && Arrays.equals(billingAddressId, customer.billingAddressId);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(userId);
        result = 31 * result + Arrays.hashCode(userId);
        result = 31 * result + Arrays.hashCode(billingAddressId);
        return result;
    }
}
