package com.omarahmed42.ecommerce.util;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.omarahmed42.ecommerce.enums.ProductSort;
import com.omarahmed42.ecommerce.exception.InvalidInputException;

public class PageUtils {
    private static final String RATING = "rating";
    private static final String PRICE = "price";
    private static final String NAME = "name";

    private PageUtils() {
    }

    public static Sort getSortOrder(String sortOrder) {
        if (sortOrder.equalsIgnoreCase("ASC"))
            return Sort.by(Direction.ASC);
        else if (sortOrder.equalsIgnoreCase("DESC"))
            return Sort.by(Direction.DESC);
        else
            throw new InvalidInputException("Sort order can either be 'asc' or 'desc'");
    }

    public static Sort getSortOrder(ProductSort sortOrder) {
        if (sortOrder == null)
            return Sort.by(Direction.DESC, RATING);

        return switch (sortOrder) {
            case RATING_ASC -> Sort.by(Direction.ASC, RATING);
            case RATING_DESC -> Sort.by(Direction.DESC, RATING);
            case PRICE_ASC -> Sort.by(Direction.ASC, PRICE);
            case PRICE_DESC -> Sort.by(Direction.DESC, PRICE);
            case NAME_ASC -> Sort.by(Direction.ASC, NAME);
            case NAME_DESC -> Sort.by(Direction.DESC, NAME);
            default -> Sort.by(Direction.DESC, RATING);
        };
    }
}
