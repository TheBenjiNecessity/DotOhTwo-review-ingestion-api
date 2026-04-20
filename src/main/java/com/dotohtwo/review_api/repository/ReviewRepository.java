package com.dotohtwo.review_api.repository;

import com.dotohtwo.models.model.Review;
import com.dotohtwo.models.model.ReviewKey;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends CassandraRepository<Review, ReviewKey> {

    Optional<Review> findByReviewId(UUID reviewId);
}
