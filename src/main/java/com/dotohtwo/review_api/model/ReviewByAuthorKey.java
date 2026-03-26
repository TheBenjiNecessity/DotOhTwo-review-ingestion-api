package com.dotohtwo.review_api.model;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@PrimaryKeyClass
public class ReviewByAuthorKey implements Serializable {

    @PrimaryKeyColumn(name = "author_id", type = PrimaryKeyType.PARTITIONED)
    private String authorId;

    @PrimaryKeyColumn(name = "created_at", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Instant createdAt;

    @PrimaryKeyColumn(name = "review_id", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private UUID reviewId;

    public ReviewByAuthorKey() {}

    public ReviewByAuthorKey(String authorId, Instant createdAt, UUID reviewId) {
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.reviewId = reviewId;
    }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public UUID getReviewId() { return reviewId; }
    public void setReviewId(UUID reviewId) { this.reviewId = reviewId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewByAuthorKey that)) return false;
        return Objects.equals(authorId, that.authorId)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(reviewId, that.reviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId, createdAt, reviewId);
    }
}
