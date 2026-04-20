package com.dotohtwo.review_api.controller;

import com.dotohtwo.review_api.dto.CreateReplyRequest;
import com.dotohtwo.review_api.dto.CreateReviewRequest;
import com.dotohtwo.models.dto.PagedReviewsResponse;
import com.dotohtwo.models.exception.DuplicateReviewException;
import com.dotohtwo.models.model.Reply;
import com.dotohtwo.models.model.Review;
import com.dotohtwo.models.model.ReviewByAuthor;
import com.dotohtwo.review_api.service.ReplyService;
import com.dotohtwo.review_api.service.ReviewService;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.List;
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

    @GetMapping
    public ResponseEntity<PagedReviewsResponse> getReviewsByAuthor(
            @RequestParam String authorId,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String pageToken) {
        ByteBuffer pagingState = pageToken != null
                ? ByteBuffer.wrap(Base64.getDecoder().decode(pageToken))
                : null;
        Slice<ReviewByAuthor> slice = reviewService.getReviewsByAuthor(authorId, pageSize, pagingState);
        String nextPageToken = null;
        if (slice.hasNext()) {
            ByteBuffer nextPagingState = ((CassandraPageRequest) slice.nextPageable()).getPagingState();
            if (nextPagingState != null) {
                nextPageToken = Base64.getEncoder().encodeToString(nextPagingState.array());
            }
        }
        return ResponseEntity.ok(new PagedReviewsResponse(slice.getContent(), nextPageToken));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable UUID reviewId) {
        return reviewService.getReview(reviewId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody CreateReviewRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(request));
        } catch (DuplicateReviewException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{reviewId}/replies")
    public ResponseEntity<List<Reply>> getRepliesForReview(@PathVariable UUID reviewId) {
        return ResponseEntity.ok(replyService.getReplies(reviewId));
    }

    @PostMapping("/{reviewId}/replies")
    public ResponseEntity<Reply> createReplyToReview(
            @PathVariable UUID reviewId,
            @RequestBody CreateReplyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.createReply(reviewId, request));
    }
}
