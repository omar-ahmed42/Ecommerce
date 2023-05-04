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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
public class ReviewComment implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    @Access(AccessType.PROPERTY)
    private UUID id;

    @Basic
    @Column(name = "title", nullable = false)
    private String title;

    @Basic
    @Column(name = "content", nullable = false)
    private String content;

    @OneToOne
    @JoinColumn(name = "product_review_id", referencedColumnName = "id", nullable = false, columnDefinition = "BINARY(16)")
    private ProductReview productReview;

    @Column(name = "created_at", updatable = false, precision = 9, scale = 6)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "modified_at")
    @LastModifiedDate
    private Instant modifiedAt;

    public ReviewComment() {
    }

    public ReviewComment(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((productReview == null) ? 0 : productReview.hashCode());
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
        ReviewComment other = (ReviewComment) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (!content.equals(other.content))
            return false;
        if (productReview == null) {
            if (other.productReview != null)
                return false;
        } else if (!productReview.equals(other.productReview))
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

    public ProductReview getProductReview() {
        return productReview;
    }

    public void setProductReview(ProductReview productReviewByProductReviewId) {
        this.productReview = productReviewByProductReviewId;
    }
}
