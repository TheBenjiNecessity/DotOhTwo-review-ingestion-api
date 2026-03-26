package com.dotohtwo.review_api;

import com.dotohtwo.review_api.repository.ReplyRepository;
import com.dotohtwo.review_api.repository.ReviewByAuthorRepository;
import com.dotohtwo.review_api.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = "spring.autoconfigure.exclude=" +
		"org.springframework.boot.cassandra.autoconfigure.CassandraAutoConfiguration," +
		"org.springframework.boot.data.cassandra.autoconfigure.DataCassandraAutoConfiguration," +
		"org.springframework.boot.data.cassandra.autoconfigure.CassandraRepositoriesAutoConfiguration"
)
class ReviewApiApplicationTests {

	@MockitoBean
	ReviewRepository reviewRepository;

	@MockitoBean
	ReviewByAuthorRepository reviewByAuthorRepository;

	@MockitoBean
	ReplyRepository replyRepository;

	@MockitoBean
	KafkaTemplate<?, ?> kafkaTemplate;

	@Test
	void contextLoads() {
	}

}
