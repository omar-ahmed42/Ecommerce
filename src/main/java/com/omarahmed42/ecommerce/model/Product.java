package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
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
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Product implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Basic
    @Column(name = "vendor_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID vendorId;

    @Basic
    @Column(name = "stock", nullable = false)
    private int stock;

    @Basic
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Basic
    @Column(name = "name", nullable = false, length = 250)
    private String name;

    @Basic
    @Column(name = "description", nullable = false)
    private String description;

    @Basic
    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Basic
    @Column(name = "modified_at")
    @LastModifiedDate
    private Instant modifiedAt;

    @Basic
    @Column(name = "rating", precision = 0)
    private Double rating;

    @OneToMany(mappedBy = "productByProductId", fetch = FetchType.LAZY)
    private Collection<CategoryProduct> categoryProductsById;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private Vendor vendorByVendorId;

    @OneToMany(mappedBy = "productByProductId", fetch = FetchType.LAZY)
    private Collection<ProductItem> productItemsById;

    @OneToMany(mappedBy = "productByProductId", fetch = FetchType.LAZY)
    private Collection<ProductMedia> productMediaById;

    @OneToMany(mappedBy = "productByProductId", fetch = FetchType.LAZY)
    private Collection<ProductReview> productReviewsById;

    @OneToMany(mappedBy = "productByProductId", fetch = FetchType.LAZY)
    private Collection<Wishlist> wishlistsById;

    @OneToMany(mappedBy = "productByProductId", fetch = FetchType.LAZY)
    private Collection<Cartitems> cartitemsById;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Product product = (Product) o;
        return stock == product.stock && Objects.equals(id, product.id) && Objects.equals(vendorId, product.vendorId)
                && Objects.equals(price, product.price) && Objects.equals(name, product.name)
                && Objects.equals(description, product.description) && Objects.equals(createdAt, product.createdAt)
                && Objects.equals(modifiedAt, product.modifiedAt) && Objects.equals(rating, product.rating);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(stock, price, name, description, createdAt, modifiedAt, rating);
        result = 31 * result + id.hashCode();
        result = 31 * result + vendorId.hashCode();
        return result;
    }
}
