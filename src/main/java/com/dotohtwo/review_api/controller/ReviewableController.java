package com.dotohtwo.review_api.controller;

import com.dotohtwo.models.model.ReviewByAuthor;
import com.dotohtwo.review_api.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reviewables")
public class ReviewableController {

    private final ReviewService reviewService;

    public ReviewableController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewByAuthor>> getReviewsByReviewable(@PathVariable String id) {
        return ResponseEntity.ok(reviewService.getCachedReviewsByReviewable(id));
    }
}
