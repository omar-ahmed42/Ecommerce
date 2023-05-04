package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

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
    private UUID id;

    @Basic
    @Column(name = "product_review_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID productReviewId;

    @Basic
    @Column(name = "title", nullable = false)
    private String title;

    @Basic
    @Column(name = "content", nullable = false)
    private String content;

    @OneToOne
    @JoinColumn(name = "product_review_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private ProductReview productReviewByProductReviewId;

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

    public UUID getProductReviewId() {
        return productReviewId;
    }

    public void setProductReviewId(UUID productReviewId) {
        this.productReviewId = productReviewId;
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ReviewComment that = (ReviewComment) o;
        return Objects.equals(id, that.id) && Objects.equals(productReviewId, that.productReviewId)
                && Objects.equals(title, that.title) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(title, content);
        result = 31 * result + id.hashCode();
        result = 31 * result + productReviewId.hashCode();
        return result;
    }

    public ProductReview getProductReviewByProductReviewId() {
        return productReviewByProductReviewId;
    }

    public void setProductReviewByProductReviewId(ProductReview productReviewByProductReviewId) {
        this.productReviewByProductReviewId = productReviewByProductReviewId;
    }
}
