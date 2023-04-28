package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicUpdate
public class User implements Serializable {
    
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Basic
    @Column(name = "email", nullable = false, length = 256, unique = true)
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return banned == user.banned && active == user.active && verified == user.verified && id.equals(user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(createdAt, user.createdAt) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(email, password, banned, active, verified, createdAt, firstName, lastName, phoneNumber);
        result = 31 * result + id.hashCode();
        return result;
    }
}
