package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.value.Gross;
import com.kinoicon.api.model.request.value.GrossRequest;
import com.kinoicon.api.model.response.value.GrossResponse;

public class GrossMapper {

    private GrossMapper() {}

    public static GrossResponse toResponse(Gross entity) {
        if (entity == null) {
            return null;
        }
        return new GrossResponse(
                PriceMapper.toResponse(entity.getRussia()),
                PriceMapper.toResponse(entity.getUsa()),
                PriceMapper.toResponse(entity.getWorld())
        );
    }

    public static Gross toEntity(GrossRequest request) {
        if (request == null) {
            return null;
        }
        return new Gross(
                PriceMapper.toEntity(request.russia()),
                PriceMapper.toEntity(request.usa()),
                PriceMapper.toEntity(request.world())
        );
    }
}
