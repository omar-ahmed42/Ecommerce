package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.CategoryProduct;
import com.omarahmed42.ecommerce.model.CategoryProductPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryProductRepository extends JpaRepository<CategoryProduct, CategoryProductPK> {

    @Override
    <S extends CategoryProduct> S save(S categoryProduct);

    @Override
    void deleteById(CategoryProductPK categoryProductPK);

    @Override
    <S extends CategoryProduct> List<S> saveAllAndFlush(Iterable<S> categoryProducts);
}
