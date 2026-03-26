package com.dotohtwo.review_api.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("reviews_by_product")
public class Review {

    @PrimaryKey
    private ReviewKey key;

    @Indexed
    @Column("review_id")
    private UUID reviewId;

    @Column("rating")
    private int rating;

    @Column("content")
    private String content;

    @Column("created_at")
    private Instant createdAt;

    public ReviewKey getKey() { return key; }
    public void setKey(ReviewKey key) { this.key = key; }

    public UUID getReviewId() { return reviewId; }
    public void setReviewId(UUID reviewId) { this.reviewId = reviewId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
