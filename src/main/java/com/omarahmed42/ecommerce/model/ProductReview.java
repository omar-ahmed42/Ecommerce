package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "product_review", schema = "ecommerce")
public class ProductReview implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private byte[] id;

    @Basic
    @Column(name = "product_id", nullable = false, updatable = false)
    private byte[] productId;

    @Basic
    @Column(name = "customer_id", nullable = false, updatable = false)
    private byte[] customerId;

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

    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    public byte[] getProductId() {
        return productId;
    }

    public void setProductId(byte[] productId) {
        this.productId = productId;
    }

    public byte[] getCustomerId() {
        return customerId;
    }

    public void setCustomerId(byte[] customerId) {
        this.customerId = customerId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductReview that = (ProductReview) o;
        return Double.compare(that.rating, rating) == 0 && Arrays.equals(id, that.id)
                && Arrays.equals(productId, that.productId) && Arrays.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rating);
        result = 31 * result + Arrays.hashCode(id);
        result = 31 * result + Arrays.hashCode(productId);
        result = 31 * result + Arrays.hashCode(customerId);
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
