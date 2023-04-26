package com.omarahmed42.ecommerce.DTO;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;

@Data
public class CategoryProductCreationDTO implements Serializable {
    private UUID productId;
    private Integer categoryId;
}
