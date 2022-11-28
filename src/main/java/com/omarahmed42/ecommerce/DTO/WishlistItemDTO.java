package com.omarahmed42.ecommerce.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemDTO {
    private BigInteger productId;
    private String name;
    private String description;
}
