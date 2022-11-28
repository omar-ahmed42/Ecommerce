package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import com.omarahmed42.ecommerce.util.UUIDHandler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicUpdate
public class User implements Serializable {
    
    @Id
    @Column(name = "id", nullable = false)
    private byte[] id;

    @Basic
    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @Basic
    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Basic
    @Column(name = "banned", nullable = false)
    private boolean banned;

    @Basic
    @Column(name = "active", nullable = false)
    private boolean active;

    @Basic
    @Column(name = "verified", nullable = false)
    private boolean verified;

    @Basic
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Basic
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Basic
    @Column(name = "first_name", nullable = false, length = 65)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false, length = 65)
    private String lastName;

    @Basic
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @OneToOne(mappedBy = "userByUserId", fetch = FetchType.LAZY)
    private BannedUser bannedUserById;

    @OneToOne(mappedBy = "userByUserId", fetch = FetchType.LAZY)
    private Customer customersById;

    @OneToOne(mappedBy = "userByUserId", fetch = FetchType.LAZY)
    private Vendor vendorsById;

    @PrePersist
    private void generateId(){
        id =  UUIDHandler.getByteArrayFromUUID(UUID.randomUUID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return banned == user.banned && active == user.active && verified == user.verified && Arrays.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(createdAt, user.createdAt) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(email, password, banned, active, verified, createdAt, firstName, lastName, phoneNumber);
        result = 31 * result + Arrays.hashCode(id);
        return result;
    }
}
