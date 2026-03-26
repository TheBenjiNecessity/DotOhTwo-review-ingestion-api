package com.dotohtwo.review_api.dto;

import com.dotohtwo.review_api.model.ReviewByAuthor;

import java.util.List;

public record PagedReviewsResponse(
        List<ReviewByAuthor> reviews,
        String nextPageToken
) {}
