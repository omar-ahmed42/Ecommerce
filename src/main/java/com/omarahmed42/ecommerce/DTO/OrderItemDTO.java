package com.omarahmed42.ecommerce.DTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class OrderItemDTO implements Serializable {
    private UUID productId;
    private UUID orderId;
    private Integer quantity;
    private BigDecimal totalPrice;
}
