package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.value.Metadata;
import com.kinoicon.api.model.response.value.MetadataResponse;

public class MetadataMapper {

    private MetadataMapper() {}

    public static MetadataResponse toResponse(Metadata entity) {
        if (entity == null) {
            return null;
        }
        return new MetadataResponse(entity.isPublished(), entity.getSchemaVersion());
    }
}
