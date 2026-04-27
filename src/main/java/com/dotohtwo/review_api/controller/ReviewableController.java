package com.dotohtwo.review_api.controller;

import com.dotohtwo.models.model.ReviewByAuthor;
import com.dotohtwo.models.model.ReviewByProduct;
import com.dotohtwo.review_api.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviewables")
public class ReviewableController {

    private final ReviewService reviewService;

    public ReviewableController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{id}/timeline")
    public ResponseEntity<List<ReviewByAuthor>> getReviewsByReviewable(@PathVariable String id) {
        return ResponseEntity.ok(reviewService.getCachedReviewsByReviewable(id));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewByProduct>> getReviewsByReviewableBetween(
            @PathVariable UUID id,
            @RequestParam Instant from,
            @RequestParam Instant to) {
        return ResponseEntity.ok(reviewService.getReviewsByProductBetween(id, from, to));
    }
}
