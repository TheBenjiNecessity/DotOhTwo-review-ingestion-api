package com.dotohtwo.review_api.config;

import com.dotohtwo.models.dto.ReviewCreatedEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, ReviewCreatedEvent> producerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        JsonMapper jsonMapper = JsonMapper.builder()
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        JacksonJsonSerializer<ReviewCreatedEvent> serializer =
                new JacksonJsonSerializer<>(jsonMapper);

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), serializer);
    }

    @Bean
    public KafkaTemplate<String, ReviewCreatedEvent> kafkaTemplate(
            ProducerFactory<String, ReviewCreatedEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
