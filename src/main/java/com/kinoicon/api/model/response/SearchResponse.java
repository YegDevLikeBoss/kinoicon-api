package com.kinoicon.api.model.response;

import com.kinoicon.api.model.response.value.FilmMinimalResponse;

import java.util.List;

public record SearchResponse(
        List<FilmMinimalResponse> films,
        List<PersonMinimalResponse> person,
        SearchResultResponse best
) {}
