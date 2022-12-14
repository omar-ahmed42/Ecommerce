package com.omarahmed42.ecommerce.model;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "category_product", schema = "ecommerce")
@IdClass(CategoryProductPK.class)
public class CategoryProduct implements Serializable{
    @Id
    @Column(name = "category_id", nullable = false, insertable = false, updatable = false)
    private int categoryId;

    @Id
    @Column(name = "product_id", nullable = false, insertable = false, updatable = false)
    private byte[] productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Category categoryByCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Product productByProductId;

    public CategoryProduct() {
    }

    public CategoryProduct(int categoryId, byte[] productId) {
        this.categoryId = categoryId;
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public byte[] getProductId() {
        return productId;
    }

    public void setProductId(byte[] productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryProduct that = (CategoryProduct) o;
        return categoryId == that.categoryId && Arrays.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(categoryId);
        result = 31 * result + Arrays.hashCode(productId);
        return result;
    }

    public Category getCategoryByCategoryId() {
        return categoryByCategoryId;
    }

    public void setCategoryByCategoryId(Category categoryByCategoryId) {
        this.categoryByCategoryId = categoryByCategoryId;
    }

    public Product getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(Product productByProductId) {
        this.productByProductId = productByProductId;
    }
}
