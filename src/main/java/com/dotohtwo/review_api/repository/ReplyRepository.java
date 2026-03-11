package com.dotohtwo.review_api.repository;

import com.dotohtwo.review_api.model.Reply;
import com.dotohtwo.review_api.model.ReplyKey;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface ReplyRepository extends CassandraRepository<Reply, ReplyKey> {
}
