package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.DTO.ReviewCommentRequest;
import com.omarahmed42.ecommerce.DTO.ReviewCommentResponse;

public interface ReviewCommentService {
    UUID addReviewComment(UUID reviewId, ReviewCommentRequest reviewCommentRequest);

    void deleteReviewComment(UUID id);

    void updateReviewComment(UUID id, ReviewCommentRequest reviewCommentRequest);

    ReviewCommentResponse getReviewComment(UUID commentId);
}
