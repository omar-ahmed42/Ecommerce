package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
    @Access(AccessType.PROPERTY)
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_details_id", columnDefinition = "BINARY(16)", referencedColumnName = "id")
    private OrderDetails orderDetails;

    public Payment() {
    }

    public Payment(String paymentIntentId, long paymentAmount, PaymentStatus paymentStatus,
            OrderDetails orderDetails) {
        this.paymentIntentId = paymentIntentId;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
        this.orderDetails = orderDetails;
    }
}
