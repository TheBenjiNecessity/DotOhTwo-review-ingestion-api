package com.dotohtwo.review_api.dto;

import java.util.UUID;

public record CreateReviewRequest(
        UUID productId,
        String authorId,
        int rating,
        String content
) {}
