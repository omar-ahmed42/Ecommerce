package com.omarahmed42.ecommerce.DTO;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class CartItemDTO {
    private BigInteger productId;
    private int quantity;
    private BigDecimal price;
}
