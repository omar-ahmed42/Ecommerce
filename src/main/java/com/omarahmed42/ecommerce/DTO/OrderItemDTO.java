package com.omarahmed42.ecommerce.DTO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class OrderItemDTO implements Serializable {
    private BigInteger productId;
    private BigInteger orderId;
    private int quantity;
    private BigDecimal totalPrice;
}
