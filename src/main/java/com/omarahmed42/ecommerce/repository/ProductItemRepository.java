package com.omarahmed42.ecommerce.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.ProductItem;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, UUID> {
    @Override
    <S extends ProductItem> S save(S entity);

    @Override
    void deleteById(UUID id);
}
