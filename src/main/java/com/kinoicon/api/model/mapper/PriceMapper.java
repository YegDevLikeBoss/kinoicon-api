package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.value.Price;
import com.kinoicon.api.model.request.value.PriceRequest;
import com.kinoicon.api.model.response.value.PriceResponse;

public class PriceMapper {

    private PriceMapper() {}

    public static PriceResponse toResponse(Price entity) {
        if (entity == null) {
            return new PriceResponse(null, null);
        }
        return new PriceResponse(entity.getAmount(), entity.getCurrency());
    }

    public static Price toEntity(PriceRequest request) {
        if (request == null) {
            return null;
        }
        return new Price(request.amount(), request.currency());
    }
}
