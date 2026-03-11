package com.dotohtwo.review_api.dto;

public record CreateReplyRequest(
        String authorId,
        String content
) {}
