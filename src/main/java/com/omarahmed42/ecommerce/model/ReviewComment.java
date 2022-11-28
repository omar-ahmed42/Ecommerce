package com.omarahmed42.ecommerce.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "review_comment", schema = "ecommerce")
public class ReviewComment implements Serializable{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false, updatable = false)
    private byte[] id;

    @Basic
    @Column(name = "product_review_id", nullable = false, updatable = false)
    private byte[] productReviewId;

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    @Basic
    @Column(name = "content", nullable = false, length = -1)
    private String content;

    @OneToOne
    @JoinColumn(name = "product_review_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private ProductReview productReviewByProductReviewId;

    public ReviewComment() {
    }

    public ReviewComment(byte[] id) {
        this.id = id;
    }

    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    public byte[] getProductReviewId() {
        return productReviewId;
    }

    public void setProductReviewId(byte[] productReviewId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewComment that = (ReviewComment) o;
        return Arrays.equals(id, that.id) && Arrays.equals(productReviewId, that.productReviewId) && Objects.equals(title, that.title) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(title, content);
        result = 31 * result + Arrays.hashCode(id);
        result = 31 * result + Arrays.hashCode(productReviewId);
        return result;
    }

    public ProductReview getProductReviewByProductReviewId() {
        return productReviewByProductReviewId;
    }

    public void setProductReviewByProductReviewId(ProductReview productReviewByProductReviewId) {
        this.productReviewByProductReviewId = productReviewByProductReviewId;
    }
}
