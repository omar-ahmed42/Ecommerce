package com.omarahmed42.ecommerce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductSort {
    PRICE_ASC("price-asc"), PRICE_DESC("price-desc"), RATING_ASC("rating-asc"), RATING_DESC("rating-desc"),
    NAME_ASC("name-asc"), NAME_DESC("name-desc");

    private final String value;

    ProductSort(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static ProductSort fromValue(String value) {
        for (ProductSort sort : ProductSort.values()) {
            if (sort.value.equals(value))
                return sort;
        }

        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
