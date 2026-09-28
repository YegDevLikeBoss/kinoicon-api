package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.value.Logline;
import com.kinoicon.api.model.request.value.LoglineRequest;
import com.kinoicon.api.model.response.value.LoglineResponse;

public class LoglineMapper {

    private LoglineMapper() {}

    public static LoglineResponse toResponse(Logline entity) {
        if (entity == null) {
            return null;
        }
        return new LoglineResponse(entity.getEn(), entity.getNative_(), entity.getRu());
    }

    public static Logline toEntity(LoglineRequest request) {
        if (request == null) {
            return null;
        }
        return new Logline(request.en(), request.native_(), request.ru());
    }
}
