package com.omarahmed42.ecommerce.DTO;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class CartItemDTO {
    private UUID productId;
    private Integer quantity;
    private BigDecimal price;
}
