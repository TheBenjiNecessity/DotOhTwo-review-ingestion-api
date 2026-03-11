package com.dotohtwo.review_api.model;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@PrimaryKeyClass
public class ReplyKey implements Serializable {

    @PrimaryKeyColumn(name = "parent_id", type = PrimaryKeyType.PARTITIONED)
    private UUID parentId;

    @PrimaryKeyColumn(name = "reply_id", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID replyId;

    public ReplyKey() {}

    public ReplyKey(UUID parentId, UUID replyId) {
        this.parentId = parentId;
        this.replyId = replyId;
    }

    public UUID getParentId() { return parentId; }
    public void setParentId(UUID parentId) { this.parentId = parentId; }

    public UUID getReplyId() { return replyId; }
    public void setReplyId(UUID replyId) { this.replyId = replyId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReplyKey that)) return false;
        return Objects.equals(parentId, that.parentId) && Objects.equals(replyId, that.replyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentId, replyId);
    }
}
