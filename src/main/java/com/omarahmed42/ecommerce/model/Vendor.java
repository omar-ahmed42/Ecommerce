package com.omarahmed42.ecommerce.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

@Entity
public class Vendor implements Serializable {
    @Id
    @Basic
    @Column(name = "user_id", nullable = false)
    private byte[] userId;

    @Column(name = "verified_vendor", nullable = false)
    private boolean verifiedVendor;

    @OneToMany(mappedBy = "vendorByVendorId", fetch = FetchType.LAZY)
    private Collection<Product> productsById;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User userByUserId;

    public Vendor() {
    }

    public Vendor(byte[] userId) {
        this.userId = userId;
    }

    public byte[] getId() {
        return this.userId;
    }

    public void setId(byte[] id) {
        this.userId = id;
    }

    public byte[] getUserId() {
        return userId;
    }

    public void setUserId(byte[] userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vendor vendor = (Vendor) o;
        return Arrays.equals(userId, vendor.userId);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(userId);
        result = 31 * result + Arrays.hashCode(userId);
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
