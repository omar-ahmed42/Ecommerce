package com.omarahmed42.ecommerce.DTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class ProductResponse implements Serializable {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

    private Set<String> mediaURLs;
    private Instant createdAt;
    private Double rating;
}
