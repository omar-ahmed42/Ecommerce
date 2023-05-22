package com.omarahmed42.ecommerce.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.ProductMedia;

@Repository
public interface ProductMediaRepository extends JpaRepository<ProductMedia, UUID> {
    void deleteAllByProductId(UUID productId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ProductMedia SET mediaUrl = :url WHERE id = :id")
    void updateURL(@Param("id") Byte id, @Param("url") String url);

    List<ProductMedia> findAllByProduct(Product product);
}