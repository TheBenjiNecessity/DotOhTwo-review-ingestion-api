package com.dotohtwo.review_api.service;

import com.dotohtwo.review_api.dto.CreateReviewRequest;
import com.dotohtwo.models.dto.ReviewCreatedEvent;
import com.dotohtwo.models.exception.DuplicateReviewException;
import com.dotohtwo.models.model.Review;
import com.dotohtwo.models.model.ReviewByAuthor;
import com.dotohtwo.models.model.ReviewByAuthorKey;
import com.dotohtwo.models.model.ReviewKey;
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
    private static final String REVIEWABLE_REVIEWS_KEY = "reviewable:%s:reviews";

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

    public List<ReviewByAuthor> getCachedReviewsByReviewable(String reviewableId) {
        String redisKey = REVIEWABLE_REVIEWS_KEY.formatted(reviewableId);
        List<Object> cached = redisTemplate.opsForList().range(redisKey, 0, -1);
        if (cached == null) return List.of();
        return cached.stream()
                .filter(ReviewByAuthor.class::isInstance)
                .map(ReviewByAuthor.class::cast)
                .toList();
    }

    public List<ReviewByAuthor> getReviewsByAuthorBetween(String authorId, Instant from, Instant to) {
        return reviewByAuthorRepository.findByKeyAuthorIdAndKeyCreatedAtBetween(authorId, from, to);
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

        String userRedisKey = USER_REVIEWS_KEY.formatted(saved.getKey().getAuthorId());
        redisTemplate.opsForList().leftPush(userRedisKey, reviewByAuthor);

        String reviewableRedisKey = REVIEWABLE_REVIEWS_KEY.formatted(saved.getKey().getProductId());
        redisTemplate.opsForList().leftPush(reviewableRedisKey, reviewByAuthor);

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
