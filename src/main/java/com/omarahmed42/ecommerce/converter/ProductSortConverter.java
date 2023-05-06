package com.omarahmed42.ecommerce.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.omarahmed42.ecommerce.enums.ProductSort;

@Component
public class ProductSortConverter implements Converter<String, ProductSort> {

    @Override
    public ProductSort convert(String source) {
        for (ProductSort sort : ProductSort.values()) {
            if (sort.getValue().equals(source))
                return sort;
        }

        throw new IllegalArgumentException("Unexpected value '" + source + "'");
    }

}
