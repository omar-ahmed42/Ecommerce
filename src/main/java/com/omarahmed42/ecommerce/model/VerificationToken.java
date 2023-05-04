package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import org.hibernate.envers.Audited;

import com.omarahmed42.ecommerce.enums.TokenStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Audited
public class VerificationToken implements Serializable {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    @Access(AccessType.PROPERTY)
    private UUID token;

    @Basic
    @Column(name = "expiry_date")
    private Timestamp expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_status")
    private TokenStatus status;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", columnDefinition = "BINARY(16)")
    private User user;

    private static final int EXPIRY_TIME_IN_MINUTES = 15;

    public VerificationToken() {
    }

    public VerificationToken(UUID token, User user) {
        this.token = token;
        this.user = user;
    }

    public VerificationToken(User user) {
        this.user = user;
    }

    @PrePersist
    private void calculateExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, EXPIRY_TIME_IN_MINUTES);
        expiryDate = new Timestamp(calendar.getTime().getTime());
    }
}
