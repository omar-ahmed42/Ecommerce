package com.omarahmed42.ecommerce.DTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

public class ProductDTO implements Serializable {
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;

    private Set<String> mediaURLs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Set<String> getMediaURLs() {
        return mediaURLs;
    }

    public void setMediaURLs(Set<String> mediaURLs) {
        this.mediaURLs = mediaURLs;
    }
}
