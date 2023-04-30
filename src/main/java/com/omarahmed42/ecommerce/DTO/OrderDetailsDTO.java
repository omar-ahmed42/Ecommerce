package com.omarahmed42.ecommerce.DTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.omarahmed42.ecommerce.enums.Status;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailsDTO implements Serializable {
    private BigDecimal totalPrice;
    private Instant purchaseDate;
    private UUID billingAddressId;
    private Status status;
}
