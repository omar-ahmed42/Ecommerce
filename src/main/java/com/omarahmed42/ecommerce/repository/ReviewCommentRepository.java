package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.ReviewComment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, byte[]> {
    @Override
    <S extends ReviewComment> S save(S entity);

    @Override
    void deleteById(byte[] id);

    @Query(value = """
            SELECT c.user_id FROM customer c, product_review pr, review_comment rc WHERE rc.id = :id AND pr.id = rc.product_review_id LIMIT 1
            """, nativeQuery = true)
    Optional<byte[]> findCustomerIdById(@Param("id") byte[] id);
}
