package com.dotohtwo.review_api.repository;

import com.dotohtwo.review_api.model.Review;
import com.dotohtwo.review_api.model.ReviewKey;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface ReviewRepository extends CassandraRepository<Review, ReviewKey> {
}
