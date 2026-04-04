package com.dotohtwo.review_api.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;

@Table("replies")
public class Reply {

    @PrimaryKey
    private ReplyKey key;

    @Column("author_id")
    private String authorId;

    @Column("content")
    private String content;

    @Column("created_at")
    private Instant createdAt;

    public ReplyKey getKey() { return key; }
    public void setKey(ReplyKey key) { this.key = key; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
