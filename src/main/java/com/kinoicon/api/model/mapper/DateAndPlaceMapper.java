package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.value.DateAndPlace;
import com.kinoicon.api.model.request.value.DateAndPlaceRequest;
import com.kinoicon.api.model.response.value.DateAndPlaceResponse;

public class DateAndPlaceMapper {

    private DateAndPlaceMapper() {}

    public static DateAndPlaceResponse toResponse(DateAndPlace entity) {
        if (entity == null) {
            return null;
        }
        return new DateAndPlaceResponse(entity.getCity(), entity.getDate());
    }

    public static DateAndPlace toEntity(DateAndPlaceRequest request) {
        if (request == null) {
            return null;
        }
        return new DateAndPlace(request.city(), request.date());
    }
}
