package com.dotohtwo.review_api.service;

import com.dotohtwo.review_api.dto.CreateReviewRequest;
import com.dotohtwo.review_api.dto.ReviewCreatedEvent;
import com.dotohtwo.review_api.exception.DuplicateReviewException;
import com.dotohtwo.review_api.model.Review;
import com.dotohtwo.review_api.model.ReviewByAuthor;
import com.dotohtwo.review_api.model.ReviewByAuthorKey;
import com.dotohtwo.review_api.model.ReviewKey;
import com.dotohtwo.review_api.repository.ReviewByAuthorRepository;
import com.dotohtwo.review_api.repository.ReviewRepository;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewService {

    private static final String USER_REVIEWS_KEY = "user:%s:reviews";

    private final ReviewRepository reviewRepository;
    private final ReviewByAuthorRepository reviewByAuthorRepository;
    private final ReviewEventProducer reviewEventProducer;
    private final RedisTemplate<String, Object> redisTemplate;

    public ReviewService(ReviewRepository reviewRepository,
                         ReviewByAuthorRepository reviewByAuthorRepository,
                         ReviewEventProducer reviewEventProducer,
                         RedisTemplate<String, Object> redisTemplate) {
        this.reviewRepository = reviewRepository;
        this.reviewByAuthorRepository = reviewByAuthorRepository;
        this.reviewEventProducer = reviewEventProducer;
        this.redisTemplate = redisTemplate;
    }

    public Optional<Review> getReview(UUID reviewId) {
        return reviewRepository.findByReviewId(reviewId);
    }

    public List<ReviewByAuthor> getCachedReviewsByAuthor(String authorId) {
        String redisKey = USER_REVIEWS_KEY.formatted(authorId);
        List<Object> cached = redisTemplate.opsForList().range(redisKey, 0, -1);
        if (cached == null) return List.of();
        return cached.stream()
                .filter(ReviewByAuthor.class::isInstance)
                .map(ReviewByAuthor.class::cast)
                .toList();
    }

    public Slice<ReviewByAuthor> getReviewsByAuthor(String authorId, int pageSize, ByteBuffer pagingState) {
        CassandraPageRequest pageRequest = pagingState != null
                ? CassandraPageRequest.of(CassandraPageRequest.first(pageSize), pagingState)
                : CassandraPageRequest.first(pageSize);
        return reviewByAuthorRepository.findByKeyAuthorId(authorId, pageRequest);
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

        ReviewByAuthor reviewByAuthor = new ReviewByAuthor();
        reviewByAuthor.setKey(new ReviewByAuthorKey(saved.getKey().getAuthorId(), saved.getCreatedAt(), saved.getReviewId()));
        reviewByAuthor.setProductId(saved.getKey().getProductId());
        reviewByAuthor.setRating(saved.getRating());
        reviewByAuthor.setContent(saved.getContent());
        reviewByAuthorRepository.save(reviewByAuthor);

        String redisKey = USER_REVIEWS_KEY.formatted(saved.getKey().getAuthorId());
        redisTemplate.opsForList().leftPush(redisKey, reviewByAuthor);

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
