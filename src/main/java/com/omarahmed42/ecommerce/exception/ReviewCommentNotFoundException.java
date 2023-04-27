package com.omarahmed42.ecommerce.exception;

public class ReviewCommentNotFoundException extends NotFoundException {
    private static final String REVIEW_COMMENT_NOT_FOUND = "Review comment not found";

    public ReviewCommentNotFoundException() {
        super(REVIEW_COMMENT_NOT_FOUND);
    }

    public ReviewCommentNotFoundException(String message) {
        super(message);
    }
}
