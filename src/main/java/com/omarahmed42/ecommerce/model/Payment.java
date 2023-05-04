package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.omarahmed42.ecommerce.enums.PaymentStatus;

import lombok.Data;

@Entity
@Data
@Table(name = "payment")
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Payment implements Serializable {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String paymentIntentId;

    @Column(name = "payment_amount")
    private long paymentAmount;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Basic
    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "order_id", columnDefinition = "BINARY(16)")
    private UUID orderId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", updatable = false, insertable = false, referencedColumnName = "id")
    private Orders orderByOrderId;

    public Payment() {
    }

    public Payment(String paymentIntentId, long paymentAmount, PaymentStatus paymentStatus,
            UUID orderId) {
        this.paymentIntentId = paymentIntentId;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
        this.orderId = orderId;
    }
}
