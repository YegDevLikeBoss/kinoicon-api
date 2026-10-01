package com.kinoicon.api.model.response;

import com.kinoicon.api.model.response.value.NameResponse;

public record FilmSummaryCrewMemberResponse(
        String id,
        Long short_id,
        NameResponse name
) {
}
