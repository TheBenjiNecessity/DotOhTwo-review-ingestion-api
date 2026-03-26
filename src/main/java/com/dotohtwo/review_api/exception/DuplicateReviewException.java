package com.dotohtwo.review_api.exception;

public class DuplicateReviewException extends RuntimeException {

    public DuplicateReviewException(String authorId, String productId) {
        super("Review already exists for authorId=" + authorId + " and productId=" + productId);
    }
}
