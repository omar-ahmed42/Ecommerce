package com.omarahmed42.ecommerce.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.ProductReview;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, UUID>{
    @Override
    <S extends ProductReview> S save(S entity);

    @Override
    void deleteById(UUID id);

    @Query(value = """
            SELECT productReview.customer_id FROM Product_Review productReview WHERE productReview.id = :id LIMIT 1
            """, nativeQuery = true)
    Optional<UUID> findCustomerIdById(@Param("id") UUID id);
}
