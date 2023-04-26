package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;

public class CategoryProductPK implements Serializable {

    public CategoryProductPK() {}

    public CategoryProductPK(int categoryId, UUID productId) {
        this.categoryId = categoryId;
        this.productId = productId;
    }

    @Column(name = "category_id", nullable = false, insertable = false, updatable = false)
    @Id
    private int categoryId;

    @Column(name = "product_id", nullable = false, insertable = false, updatable = false)
    @Id
    private UUID productId;

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
        CategoryProductPK that = (CategoryProductPK) o;
        return categoryId == that.categoryId && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(categoryId);
        result = 31 * result + productId.hashCode();
        return result;
    }
}
