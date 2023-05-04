package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class ProductMedia implements Serializable{
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Basic
    @Column(name = "product_id", nullable = false, insertable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID productId;

    @Basic
    @Column(name = "media_url", nullable = true)
    private String mediaUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Product productByProductId;

    public ProductMedia() {
    }

    public ProductMedia(UUID productId, String mediaUrl) {
        this.productId = productId;
        this.mediaUrl = mediaUrl;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductMedia that = (ProductMedia) o;
        return Objects.equals(id, that.id) && Objects.equals(productId, that.productId) && Objects.equals(mediaUrl, that.mediaUrl);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mediaUrl);
        result = 31 * result + id.hashCode();
        result = 31 * result + productId.hashCode();
        return result;
    }

    public Product getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(Product productByProductId) {
        this.productByProductId = productByProductId;
    }
}
