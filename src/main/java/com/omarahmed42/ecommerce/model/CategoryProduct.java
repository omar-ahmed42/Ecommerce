package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Entity
@IdClass(CategoryProductPK.class)
@Audited
public class CategoryProduct implements Serializable{
    @Id
    @Column(name = "category_id", nullable = false, insertable = false, updatable = false)
    private int categoryId;

    @Id
    @Column(name = "product_id", nullable = false, insertable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Product product;

    public CategoryProduct() {
    }

    public CategoryProduct(int categoryId, UUID productId) {
        this.categoryId = categoryId;
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryProduct that = (CategoryProduct) o;
        return categoryId == that.categoryId && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(categoryId);
        result = 31 * result + productId.hashCode();
        return result;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category categoryByCategoryId) {
        this.category = categoryByCategoryId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product productByProductId) {
        this.product = productByProductId;
    }
}
