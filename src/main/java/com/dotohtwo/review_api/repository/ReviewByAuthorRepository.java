package com.dotohtwo.review_api.repository;

import com.dotohtwo.models.model.ReviewByAuthor;
import com.dotohtwo.models.model.ReviewByAuthorKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewByAuthorRepository extends CassandraRepository<ReviewByAuthor, ReviewByAuthorKey> {

    Slice<ReviewByAuthor> findByKeyAuthorId(String authorId, Pageable pageable);
}
