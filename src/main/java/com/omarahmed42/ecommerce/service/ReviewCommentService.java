package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.ReviewComment;

public interface ReviewCommentService {
    void addReviewComment(ReviewComment reviewComment);
    void deleteReviewComment(ReviewComment reviewComment);
    void updateReviewComment(ReviewComment reviewComment);
    ReviewComment getReviewComment(byte[] commentId);
    byte[] findCustomerIdById(byte[] commentId);
}
