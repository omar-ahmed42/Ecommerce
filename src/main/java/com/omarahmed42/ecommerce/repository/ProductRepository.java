package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, byte[]> {

    @Override
    boolean existsById(byte[] id);

    @Override
    <S extends Product> S save(S entity);

    @Override
    void deleteById(byte[] id);

    @Override
    void deleteAllInBatch(Iterable<Product> entities);
}
