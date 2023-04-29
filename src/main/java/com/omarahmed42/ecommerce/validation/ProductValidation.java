package com.omarahmed42.ecommerce.validation;

import java.math.BigDecimal;

import org.springframework.util.ObjectUtils;
import io.micrometer.core.instrument.util.StringUtils;

import com.omarahmed42.ecommerce.DTO.ProductRequest;
import com.omarahmed42.ecommerce.exception.InvalidInputException;
import com.omarahmed42.ecommerce.exception.MissingFieldException;

public class ProductValidation {
    private ProductValidation() {}

    public static void validateProduct(ProductRequest productRequest) {
        if (StringUtils.isBlank(productRequest.getName()))
            throw new MissingFieldException("Product name is missing");
        if (StringUtils.isBlank(productRequest.getDescription()))
            throw new MissingFieldException("Product description is missing");
        if (ObjectUtils.isEmpty(productRequest.getStock()))
            throw new MissingFieldException("Product stock is missing");
        if (productRequest.getStock() < 0)
            throw new InvalidInputException("Product stock cannot be less than 0");
        if (ObjectUtils.isEmpty(productRequest.getPrice()))
            throw new MissingFieldException("Product price is missing");
        if (productRequest.getPrice().compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidInputException("Product price cannot be less than 0");
    }
}
