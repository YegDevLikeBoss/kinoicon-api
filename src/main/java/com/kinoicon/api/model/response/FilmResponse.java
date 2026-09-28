package com.kinoicon.api.model.response;

import com.kinoicon.api.model.response.value.*;

import java.time.LocalDate;
import java.util.List;

/** "full" size - mirrors the Film schema in the OpenAPI spec. */
public record FilmResponse(
        String id,
        Long short_id,
        NameResponse name,
        LocalDate date,
        Integer duration,
        LoglineResponse logline,
        PriceResponse budget,
        GrossResponse gross,
        ExternalResponse external,
        RatingResponse rating,
        FilmMediaResponse media,
        List<CrewMemberResponse> crew,
        MetadataResponse metadata
) {}
