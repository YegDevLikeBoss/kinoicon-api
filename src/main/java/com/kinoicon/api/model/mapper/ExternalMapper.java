package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.value.External;
import com.kinoicon.api.model.request.value.ExternalRequest;
import com.kinoicon.api.model.response.value.ExternalResponse;

public class ExternalMapper {

    private ExternalMapper() {}

    public static ExternalResponse toResponse(External entity) {
        if (entity == null) {
            return new ExternalResponse(null, null);
        }
        return new ExternalResponse(entity.getImdbId(), entity.getKpId());
    }

    public static External toEntity(ExternalRequest request) {
        if (request == null) {
            return null;
        }
        return new External(request.imdb_id(), request.kp_id());
    }
}
