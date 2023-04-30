package com.omarahmed42.ecommerce.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.Category;
import com.omarahmed42.ecommerce.model.CategoryProduct;
import com.omarahmed42.ecommerce.model.CategoryProductPK;

@Repository
public interface CategoryProductRepository extends JpaRepository<CategoryProduct, CategoryProductPK> {

    @Override
    <S extends CategoryProduct> S save(S categoryProduct);

    @Override
    void deleteById(CategoryProductPK categoryProductPK);

    @Override
    <S extends CategoryProduct> List<S> saveAllAndFlush(Iterable<S> categoryProducts);

    @Query(value = "SELECT categoryByCategoryId FROM CategoryProduct cp WHERE cp.productId = :product_id")
    List<Category> findCategoriesByProductId(@Param("product_id") UUID productId);
}
