package com.dotohtwo.review_api.controller;

import com.dotohtwo.review_api.dto.CreateReplyRequest;
import com.dotohtwo.review_api.dto.CreateReviewRequest;
import com.dotohtwo.review_api.exception.DuplicateReviewException;
import com.dotohtwo.review_api.model.Reply;
import com.dotohtwo.review_api.model.Review;
import com.dotohtwo.review_api.service.ReplyService;
import com.dotohtwo.review_api.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReplyService replyService;

    public ReviewController(ReviewService reviewService, ReplyService replyService) {
        this.reviewService = reviewService;
        this.replyService = replyService;
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody CreateReviewRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(request));
        } catch (DuplicateReviewException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/{reviewId}/replies")
    public ResponseEntity<Reply> createReplyToReview(
            @PathVariable UUID reviewId,
            @RequestBody CreateReplyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.createReply(reviewId, request));
    }
}
