package com.omarahmed42.ecommerce.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.ReviewComment;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, UUID> {
    @Query(value = """
            SELECT c.user_id FROM customer c, product_review pr, review_comment rc WHERE rc.id = :id AND pr.id = rc.product_review_id LIMIT 1
            """, nativeQuery = true)
    Optional<UUID> findCustomerIdById(@Param("id") UUID id);
}