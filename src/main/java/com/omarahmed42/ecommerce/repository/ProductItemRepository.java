package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, byte[]> {
    @Override
    <S extends ProductItem> S save(S entity);

    @Override
    void deleteById(byte[] id);
}
