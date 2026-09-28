package com.kinoicon.api.model.request;

import com.kinoicon.api.model.request.value.ImageWithNoteRequest;
import com.kinoicon.api.model.request.value.TrailerRequest;

import java.util.List;

public record FilmMediaRequest(
        String cover_url,
        List<ImageWithNoteRequest> images,
        List<TrailerRequest> trailers
) {
}
