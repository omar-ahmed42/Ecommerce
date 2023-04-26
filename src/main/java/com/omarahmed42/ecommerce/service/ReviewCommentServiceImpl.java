package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.exception.CustomerNotFoundException;
import com.omarahmed42.ecommerce.exception.ReviewCommentNotFoundException;
import com.omarahmed42.ecommerce.model.ReviewComment;
import com.omarahmed42.ecommerce.repository.ReviewCommentRepository;

@Service
public class ReviewCommentServiceImpl implements ReviewCommentService {

    private final ReviewCommentRepository reviewCommentRepository;

    public ReviewCommentServiceImpl(ReviewCommentRepository reviewCommentRepository) {
        this.reviewCommentRepository = reviewCommentRepository;
    }

    @Transactional
    @Override
    public void addReviewComment(ReviewComment reviewComment) {
        reviewCommentRepository.save(reviewComment);
    }

    @Transactional
    @Override
    public void deleteReviewComment(ReviewComment reviewComment) {
        reviewCommentRepository
                .findById(reviewComment.getId())
                .ifPresentOrElse(reviewCommentRepository::delete,
                        () -> {
                            throw new ReviewCommentNotFoundException("Review comment not found");
                        });
    }

    @Transactional
    @Override
    public void updateReviewComment(ReviewComment reviewComment) {
        reviewCommentRepository
                .findById(reviewComment.getId())
                .ifPresentOrElse(present -> {
                    ModelMapper modelMapper = new ModelMapper();
                    modelMapper.getConfiguration().setSkipNullEnabled(true);
                    modelMapper.map(reviewComment, present);
                    reviewCommentRepository.save(present);
                },
                        () -> {
                            throw new ReviewCommentNotFoundException("Review comment not found");
                        });
    }

    @Transactional
    @Override
    public ReviewComment getReviewComment(UUID commentId) {
        return reviewCommentRepository
                .findById(commentId)
                .orElseThrow(() -> new ReviewCommentNotFoundException("Review comment no found"));
    }

    @Override
    public UUID findCustomerIdById(UUID commentId) {
        return reviewCommentRepository.findCustomerIdById(commentId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found for comment with id: " + commentId.toString()));
    }
}
