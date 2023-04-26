package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Vendor implements Serializable {
    @Id
    @Basic
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "verified_vendor", nullable = false)
    private boolean verifiedVendor;

    @OneToMany(mappedBy = "vendorByVendorId", fetch = FetchType.LAZY)
    private Collection<Product> productsById;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User userByUserId;

    public Vendor() {
    }

    public Vendor(UUID userId) {
        this.userId = userId;
    }

    public UUID getId() {
        return this.userId;
    }

    public void setId(UUID id) {
        this.userId = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vendor vendor = (Vendor) o;
        return Objects.equals(userId, vendor.userId);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }

    public Collection<Product> getProductsById() {
        return productsById;
    }

    public void setProductsById(Collection<Product> productsById) {
        this.productsById = productsById;
    }

    public User getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
    }

    public boolean isVerifiedVendor() {
        return verifiedVendor;
    }

    public void setVerifiedVendor(boolean verifiedVendor) {
        this.verifiedVendor = verifiedVendor;
    }
}
