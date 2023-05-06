package com.omarahmed42.ecommerce.specification;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Queue;

import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import com.omarahmed42.ecommerce.DTO.ProductFilter;
import com.omarahmed42.ecommerce.model.Product;

public class ProductSpecification {
    private ProductSpecification() {
    }

    public static Specification<Product> hasName(String name) {
        return (root, query, cb) -> cb.equal(root.get("name"), name);
    }

    public static Specification<Product> hasPriceBetween(BigDecimal low, BigDecimal high) {
        return (root, query, cb) -> priceAtLeast(low).and(priceAtMost(high)).toPredicate(root, query, cb);
    }

    public static Specification<Product> priceAtLeast(BigDecimal price) {
        return (root, query, cb) -> (price == null) ? cb.conjunction() : cb.ge(root.get("price"), price);
    }

    public static Specification<Product> priceAtMost(BigDecimal price) {
        return (root, query, cb) -> (price == null) ? cb.conjunction() : cb.le(root.get("price"), price);
    }

    public static Specification<Product> hasCategory(Integer id) {
        return (root, query, cb) -> (id == null) ? cb.conjunction()
                : cb.equal(root.join("categories", JoinType.INNER).get("id"), id);
    }

    public static Specification<Product> buildSpecification(ProductFilter filter) {
        Queue<Specification<Product>> specifications = new LinkedList<>();
        specifications.add(ProductSpecification.hasName(filter.getName()));
        specifications.add(ProductSpecification.hasPriceBetween(filter.getLowPrice(), filter.getHighPrice()));
        specifications.add(ProductSpecification.hasCategory(filter.getCategory()));

        Specification<Product> result = specifications.poll();
        while (!specifications.isEmpty()) {
            Specification<Product> current = specifications.poll();
            result = Specification.where(result.and(current));
        }
        return result;
    }
}
