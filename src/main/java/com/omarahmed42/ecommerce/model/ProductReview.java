package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
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

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "product_review", schema = "ecommerce")
@EntityListeners(AuditingEntityListener.class)
@Audited
public class ProductReview implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    @Access(AccessType.PROPERTY)
    private UUID id;

    @Basic
    @Column(name = "rating", nullable = false, precision = 0)
    private double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", columnDefinition = "BINARY(16)")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id", columnDefinition = "BINARY(16)")
    private Customer customer;

    @OneToOne(mappedBy = "productReview", fetch = FetchType.LAZY, optional = true)
    private ReviewComment reviewComment;

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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        long temp;
        temp = Double.doubleToLongBits(rating);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + ((customer == null) ? 0 : customer.hashCode());
        result = prime * result + ((reviewComment == null) ? 0 : reviewComment.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((modifiedAt == null) ? 0 : modifiedAt.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductReview other = (ProductReview) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (Double.doubleToLongBits(rating) != Double.doubleToLongBits(other.rating))
            return false;
        if (product == null) {
            if (other.product != null)
                return false;
        } else if (!product.equals(other.product))
            return false;
        if (customer == null) {
            if (other.customer != null)
                return false;
        } else if (!customer.equals(other.customer))
            return false;
        if (reviewComment == null) {
            if (other.reviewComment != null)
                return false;
        } else if (!reviewComment.equals(other.reviewComment))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        if (modifiedAt == null) {
            if (other.modifiedAt != null)
                return false;
        } else if (!modifiedAt.equals(other.modifiedAt))
            return false;
        return true;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product productByProductId) {
        this.product = productByProductId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customerByCustomerId) {
        this.customer = customerByCustomerId;
    }

    public ReviewComment getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(ReviewComment reviewCommentsById) {
        this.reviewComment = reviewCommentsById;
    }
}
