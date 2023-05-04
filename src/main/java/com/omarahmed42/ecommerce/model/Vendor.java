package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class Vendor implements Serializable {
    @Id
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    @Access(AccessType.PROPERTY)
    private UUID id;

    @Column(name = "verified_vendor", nullable = false)
    private boolean verifiedVendor;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id", columnDefinition = "BINARY(16)")
    private User user;

    public Vendor() {
    }

    public Vendor(UUID userId) {
        this.id = userId;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return id;
    }

    public void setUserId(UUID userId) {
        this.id = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Vendor vendor = (Vendor) o;
        return Objects.equals(id, vendor.id);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userByUserId) {
        this.user = userByUserId;
    }

    public boolean isVerifiedVendor() {
        return verifiedVendor;
    }

    public void setVerifiedVendor(boolean verifiedVendor) {
        this.verifiedVendor = verifiedVendor;
    }
}
