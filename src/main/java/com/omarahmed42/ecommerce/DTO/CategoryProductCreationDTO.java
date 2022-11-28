package com.omarahmed42.ecommerce.DTO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class CategoryProductCreationDTO implements Serializable {
    private BigInteger productId;
    private int categoryId;
}
