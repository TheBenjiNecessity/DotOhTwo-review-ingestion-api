package com.dotohtwo.review_api.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("reviews_by_author")
public class ReviewByAuthor {

    @PrimaryKey
    private ReviewByAuthorKey key;

    @Column("product_id")
    private UUID productId;

    @Column("rating")
    private int rating;

    @Column("content")
    private String content;

    public ReviewByAuthorKey getKey() { return key; }
    public void setKey(ReviewByAuthorKey key) { this.key = key; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
