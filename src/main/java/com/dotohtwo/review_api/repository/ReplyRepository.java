package com.dotohtwo.review_api.repository;

import com.dotohtwo.review_api.model.Reply;
import com.dotohtwo.review_api.model.ReplyKey;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReplyRepository extends CassandraRepository<Reply, ReplyKey> {

    List<Reply> findByKeyParentId(UUID parentId);

    Optional<Reply> findByReplyId(UUID replyId);
}
