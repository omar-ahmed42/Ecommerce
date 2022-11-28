package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.ProductMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMediaRepository extends JpaRepository<ProductMedia, byte[]> {
    @Override
    <S extends ProductMedia> List<S> saveAll(Iterable<S> entities);

    @Override
    <S extends ProductMedia> S save(S entity);

    @Override
    void deleteById(byte[] id);

    void deleteAllByProductId(byte[] productId);
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ProductMedia SET mediaUrl = :url WHERE id = :id")
    void updateURL(@Param("id") Byte id, @Param("url") String url);
}
