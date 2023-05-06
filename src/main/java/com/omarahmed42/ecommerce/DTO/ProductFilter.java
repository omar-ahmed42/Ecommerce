package com.omarahmed42.ecommerce.DTO;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductFilter implements Serializable {
    @JsonProperty(value = "category")
    private Integer category;
    @JsonProperty(value = "low-price")
    private BigDecimal lowPrice;
    @JsonProperty(value = "high-price")
    private BigDecimal highPrice;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "atleast-rating")
    private Double atleastRating;
}
