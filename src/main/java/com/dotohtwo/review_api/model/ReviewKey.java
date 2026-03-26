package com.dotohtwo.review_api.model;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;

import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@PrimaryKeyClass
public class ReviewKey implements Serializable {

    @PrimaryKeyColumn(name = "product_id", type = PrimaryKeyType.PARTITIONED)
    private UUID productId;

    @PrimaryKeyColumn(name = "author_id", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private String authorId;

    public ReviewKey() {}

    public ReviewKey(UUID productId, String authorId) {
        this.productId = productId;
        this.authorId = authorId;
    }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewKey that)) return false;
        return Objects.equals(productId, that.productId) && Objects.equals(authorId, that.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, authorId);
    }
}
