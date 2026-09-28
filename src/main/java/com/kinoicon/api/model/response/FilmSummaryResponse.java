package com.kinoicon.api.model.response;

import com.kinoicon.api.model.response.value.NameResponse;

import java.time.LocalDate;

public record FilmSummaryResponse(
        String id,
        Long short_id,
        NameResponse name,
        LocalDate date,
        String cover_url,
        String director,
        String main_actors
) {}
