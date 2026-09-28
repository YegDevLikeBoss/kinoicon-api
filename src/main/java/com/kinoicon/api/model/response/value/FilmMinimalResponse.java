package com.kinoicon.api.model.response.value;

import java.time.LocalDate;

public record FilmMinimalResponse(String id, Long short_id, NameResponse name, LocalDate date, String cover_url) {}
