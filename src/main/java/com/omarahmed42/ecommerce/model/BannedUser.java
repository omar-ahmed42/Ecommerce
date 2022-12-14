package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "banned_user", schema = "ecommerce")
@EntityListeners(AuditingEntityListener.class)
public class BannedUser implements Serializable {
    @Id
    @Column(name = "user_id", nullable = false)
    private byte[] userId;

    @Basic
    @Column(name = "ban_reason", nullable = false, length = -1)
    private String banReason;

    @Basic
    @Column(name = "ip", nullable = true)
    private String ip;

    @Basic
    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

    @Basic
    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private Instant createdAt;

    @Basic
    @Column(name = "modified_at", nullable = false)
    @LastModifiedDate
    private Instant modifiedAt;

    @Basic
    @Column(name = "number_of_violations", nullable = true)
    private Integer numberOfViolations;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private User userByUserId;

    public byte[] getUserId() {
        return userId;
    }

    public void setUserId(byte[] userId) {
        this.userId = userId;
    }

    public String getBanReason() {
        return banReason;
    }

    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Integer getNumberOfViolations() {
        return numberOfViolations;
    }

    public void setNumberOfViolations(Integer numberOfViolations) {
        this.numberOfViolations = numberOfViolations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BannedUser that = (BannedUser) o;
        return Arrays.equals(userId, that.userId) && Objects.equals(banReason, that.banReason) && Objects.equals(ip, that.ip) && Objects.equals(expirationDate, that.expirationDate) && Objects.equals(createdAt, that.createdAt) && Objects.equals(modifiedAt, that.modifiedAt) && Objects.equals(numberOfViolations, that.numberOfViolations);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(banReason, ip, expirationDate, createdAt, modifiedAt, numberOfViolations);
        result = 31 * result + Arrays.hashCode(userId);
        return result;
    }

    public User getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
    }
}
