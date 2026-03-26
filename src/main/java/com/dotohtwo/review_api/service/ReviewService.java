package com.dotohtwo.review_api.service;

import com.dotohtwo.review_api.dto.CreateReviewRequest;
import com.dotohtwo.review_api.dto.ReviewCreatedEvent;
import com.dotohtwo.review_api.exception.DuplicateReviewException;
import com.dotohtwo.review_api.model.Review;
import com.dotohtwo.review_api.model.ReviewKey;
import com.dotohtwo.review_api.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewEventProducer reviewEventProducer;

    public ReviewService(ReviewRepository reviewRepository, ReviewEventProducer reviewEventProducer) {
        this.reviewRepository = reviewRepository;
        this.reviewEventProducer = reviewEventProducer;
    }

    public Review createReview(CreateReviewRequest request) {
        ReviewKey key = new ReviewKey(request.productId(), request.authorId());
        if (reviewRepository.existsById(key)) {
            throw new DuplicateReviewException(request.authorId(), request.productId().toString());
        }

        Review review = new Review();
        review.setKey(key);
        review.setReviewId(UUID.randomUUID());
        review.setRating(request.rating());
        review.setContent(request.content());
        review.setCreatedAt(Instant.now());

        Review saved = reviewRepository.save(review);

        reviewEventProducer.publishReviewCreated(new ReviewCreatedEvent(
                saved.getReviewId(),
                saved.getKey().getProductId(),
                saved.getKey().getAuthorId(),
                saved.getRating(),
                saved.getContent(),
                saved.getCreatedAt()
        ));

        return saved;
    }
}
