package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.ProductReview;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, byte[]>{
    @Override
    <S extends ProductReview> S save(S entity);

    @Override
    void deleteById(byte[] id);

    @Query(value = """
            SELECT productReview.customer_id FROM Product_Review productReview WHERE productReview.id = :id LIMIT 1
            """, nativeQuery = true)
    Optional<byte[]> findCustomerIdById(@Param("id") byte[] id);
}
