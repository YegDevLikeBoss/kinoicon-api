package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.value.Rating;
import com.kinoicon.api.model.entity.value.RatingItem;
import com.kinoicon.api.model.response.value.RatingItemResponse;
import com.kinoicon.api.model.response.value.RatingResponse;

public class RatingMapper {

    private RatingMapper() {}

    public static RatingResponse toResponse(Rating entity) {
        if (entity == null) {
            return null;
        }
        return new RatingResponse(
                toItemResponse(entity.getImdb()),
                toItemResponse(entity.getKp()),
                entity.getMetacritic(),
                entity.getRottentomatos()
        );
    }

    private static RatingItemResponse toItemResponse(RatingItem item) {
        if (item == null) {
            return null;
        }
        return new RatingItemResponse(item.getRating(), item.getVoteCount());
    }

    // Rating is readOnly in the API contract - it's derived server-side (KP rating sync),
    // never accepted from the client, so there is intentionally no toEntity(RatingRequest).
}
