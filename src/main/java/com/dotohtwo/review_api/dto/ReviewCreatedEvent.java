package com.dotohtwo.review_api.dto;

import java.time.Instant;
import java.util.UUID;

public record ReviewCreatedEvent(
        UUID reviewId,
        UUID productId,
        String authorId,
        int rating,
        String content,
        Instant createdAt
) {}
