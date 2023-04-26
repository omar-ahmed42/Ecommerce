package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.model.ReviewComment;

public interface ReviewCommentService {
    void addReviewComment(ReviewComment reviewComment);
    void deleteReviewComment(ReviewComment reviewComment);
    void updateReviewComment(ReviewComment reviewComment);
    ReviewComment getReviewComment(UUID commentId);
    UUID findCustomerIdById(UUID commentId);
}
