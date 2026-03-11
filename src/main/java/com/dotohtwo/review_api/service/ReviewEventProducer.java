package com.dotohtwo.review_api.service;

import com.dotohtwo.review_api.dto.ReviewCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReviewEventProducer {

    private static final Logger log = LoggerFactory.getLogger(ReviewEventProducer.class);

    private final KafkaTemplate<String, ReviewCreatedEvent> kafkaTemplate;
    private final String reviewsTopic;

    public ReviewEventProducer(
            KafkaTemplate<String, ReviewCreatedEvent> kafkaTemplate,
            @Value("${kafka.topics.reviews}") String reviewsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.reviewsTopic = reviewsTopic;
    }

    public void publishReviewCreated(ReviewCreatedEvent event) {
        kafkaTemplate.send(reviewsTopic, event.productId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish review event for reviewId={}", event.reviewId(), ex);
                    } else {
                        log.debug("Published review event for reviewId={} to partition {}",
                                event.reviewId(), result.getRecordMetadata().partition());
                    }
                });
    }
}
