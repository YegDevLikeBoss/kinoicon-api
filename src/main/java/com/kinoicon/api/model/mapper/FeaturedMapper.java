package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.FeaturedEntity;
import com.kinoicon.api.model.entity.FeaturedRowEntity;
import com.kinoicon.api.model.entity.FeaturedRowItemEntity;
import com.kinoicon.api.model.response.FeaturedItemResponse;
import com.kinoicon.api.model.response.FeaturedResponse;
import com.kinoicon.api.model.response.FeaturedRowResponse;

import java.util.List;

/**
 * Entity <-> DTO mapping for the Featured singleton.
 * Item.id (a UUID) is not stored on featured_row_item - it references a film or person by
 * item_short_id, so the service must resolve the referenced entity's uuid before this
 * mapper can build the final FeaturedItemResponse. That's why toItemResponse takes the
 * resolved uuid as a plain argument rather than looking it up itself.
 */
public class FeaturedMapper {

    private FeaturedMapper() {}

    public static FeaturedItemResponse toItemResponse(FeaturedRowItemEntity entity, String resolvedUuid) {
        return new FeaturedItemResponse(resolvedUuid, entity.getItemKind(), entity.getItemShortId());
    }

    public static FeaturedRowResponse toRowResponse(FeaturedRowEntity entity, List<FeaturedItemResponse> items) {
        return new FeaturedRowResponse(entity.getKind(), entity.getTitle(), items);
    }

    public static FeaturedResponse toResponse(FeaturedEntity entity, List<FeaturedRowResponse> rows) {
        return new FeaturedResponse(entity.getTitle(), rows);
    }
}
