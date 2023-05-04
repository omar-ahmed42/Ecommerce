package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
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
@Audited
public class Product implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    @Access(AccessType.PROPERTY)
    private UUID id;

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
    @Column(name = "created_at", updatable = false, precision = 9, scale = 6)
    @CreatedDate
    private Instant createdAt;

    @Basic
    @Column(name = "modified_at")
    @LastModifiedDate
    private Instant modifiedAt;

    @Basic
    @Column(name = "rating", precision = 0)
    private Double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", referencedColumnName = "user_id", columnDefinition = "BINARY(16)")
    private Vendor vendor;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Collection<ProductMedia> productMedia = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "product_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public void addProductMedia(ProductMedia productMedia) {
        this.productMedia.add(productMedia);
        productMedia.setProduct(this);
    }

    public void deleteProductMedia(ProductMedia productMedia) {
        this.productMedia.remove(productMedia);
        productMedia.setProduct(null);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + stock;
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((modifiedAt == null) ? 0 : modifiedAt.hashCode());
        result = prime * result + ((rating == null) ? 0 : rating.hashCode());
        result = prime * result + ((vendor == null) ? 0 : vendor.hashCode());
        result = prime * result + ((productMedia == null) ? 0 : productMedia.hashCode());
        result = prime * result + ((categories == null) ? 0 : categories.hashCode());
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
        Product other = (Product) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (stock != other.stock)
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
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
        if (rating == null) {
            if (other.rating != null)
                return false;
        } else if (!rating.equals(other.rating))
            return false;
        if (vendor == null) {
            if (other.vendor != null)
                return false;
        } else if (!vendor.equals(other.vendor))
            return false;
        if (productMedia == null) {
            if (other.productMedia != null)
                return false;
        } else if (!productMedia.equals(other.productMedia))
            return false;
        if (categories == null) {
            if (other.categories != null)
                return false;
        } else if (!categories.equals(other.categories))
            return false;
        return true;
    }
}
