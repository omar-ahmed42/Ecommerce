package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "product_review", schema = "ecommerce")
@EntityListeners(AuditingEntityListener.class)
public class ProductReview implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, insertable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Basic
    @Column(name = "product_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID productId;

    @Basic
    @Column(name = "customer_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID customerId;

    @Basic
    @Column(name = "rating", nullable = false, precision = 0)
    private double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Product productByProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id", insertable = false, updatable = false, nullable = false)
    private Customer customerByCustomerId;

    @OneToOne(mappedBy = "productReviewByProductReviewId", fetch = FetchType.LAZY, optional = true)
    private ReviewComment reviewCommentsById;

    @Column(name = "created_at", updatable = false, precision = 9, scale = 6)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "modified_at")
    @LastModifiedDate
    private Instant modifiedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductReview that = (ProductReview) o;
        return Double.compare(that.rating, rating) == 0 && Objects.equals(id, that.id)
                && Objects.equals(productId, that.productId) && Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rating);
        result = 31 * result + id.hashCode();
        result = 31 * result + productId.hashCode();
        result = 31 * result + customerId.hashCode();
        return result;
    }

    public Product getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(Product productByProductId) {
        this.productByProductId = productByProductId;
    }

    public Customer getCustomerByCustomerId() {
        return customerByCustomerId;
    }

    public void setCustomerByCustomerId(Customer customerByCustomerId) {
        this.customerByCustomerId = customerByCustomerId;
    }

    public ReviewComment getReviewCommentsById() {
        return reviewCommentsById;
    }

    public void setReviewCommentsById(ReviewComment reviewCommentsById) {
        this.reviewCommentsById = reviewCommentsById;
    }
}
