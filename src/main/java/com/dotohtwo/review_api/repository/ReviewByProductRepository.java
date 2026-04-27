package com.dotohtwo.review_api.repository;

import com.dotohtwo.models.model.ReviewByProduct;
import com.dotohtwo.models.model.ReviewByProductKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReviewByProductRepository extends CassandraRepository<ReviewByProduct, ReviewByProductKey> {

    @Query("SELECT * FROM reviews_by_product WHERE product_id = ?0 AND created_at >= ?1 AND created_at <= ?2")
    List<ReviewByProduct> findByProductIdAndCreatedAtBetween(UUID productId, Instant from, Instant to);
}
