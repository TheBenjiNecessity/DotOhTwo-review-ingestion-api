package com.dotohtwo.review_api.service;

import com.dotohtwo.review_api.dto.CreateReviewRequest;
import com.dotohtwo.review_api.dto.ReviewCreatedEvent;
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
        Review review = new Review();
        review.setKey(new ReviewKey(request.productId(), UUID.randomUUID()));
        review.setAuthorId(request.authorId());
        review.setRating(request.rating());
        review.setContent(request.content());
        review.setCreatedAt(Instant.now());

        Review saved = reviewRepository.save(review);

        reviewEventProducer.publishReviewCreated(new ReviewCreatedEvent(
                saved.getKey().getReviewId(),
                saved.getKey().getProductId(),
                saved.getAuthorId(),
                saved.getRating(),
                saved.getContent(),
                saved.getCreatedAt()
        ));

        return saved;
    }
}
