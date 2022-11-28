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
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "product_media", schema = "ecommerce")
public class ProductMedia implements Serializable{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false)
    private byte[] id;

    @Basic
    @Column(name = "product_id", nullable = false, insertable = false, updatable = false)
    private byte[] productId;

    @Basic
    @Column(name = "media_url", nullable = true, length = -1)
    private String mediaUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Product productByProductId;

    public ProductMedia() {
    }

    public ProductMedia(byte[] productId, String mediaUrl) {
        this.productId = productId;
        this.mediaUrl = mediaUrl;
    }

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
        return Arrays.equals(id, that.id) && Arrays.equals(productId, that.productId) && Objects.equals(mediaUrl, that.mediaUrl);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mediaUrl);
        result = 31 * result + Arrays.hashCode(id);
        result = 31 * result + Arrays.hashCode(productId);
        return result;
    }

    public Product getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(Product productByProductId) {
        this.productByProductId = productByProductId;
    }
}
