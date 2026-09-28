package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.value.Name;
import com.kinoicon.api.model.request.value.NameRequest;
import com.kinoicon.api.model.response.value.NameResponse;

public class NameMapper {

    private NameMapper() {}

    public static NameResponse toResponse(Name entity) {
        if (entity == null) {
            return null;
        }
        return new NameResponse(entity.getEn(), entity.getNative_(), entity.getRu());
    }

    public static Name toEntity(NameRequest request) {
        if (request == null) {
            return null;
        }
        return new Name(request.en(), request.native_(), request.ru());
    }
}
