package com.kinoicon.api.model.response;

import com.kinoicon.api.model.response.value.NameResponse;

import java.time.LocalDate;
import java.util.List;

public record FilmSummaryResponse(
        String id,
        Long short_id,
        NameResponse name,
        LocalDate date,
        String cover_url,
        FilmSummaryCrewMemberResponse director,
        List<FilmSummaryCrewMemberResponse> main_actors
) {}
