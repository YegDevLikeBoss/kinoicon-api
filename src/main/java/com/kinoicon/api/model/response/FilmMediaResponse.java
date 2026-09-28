package com.kinoicon.api.model.response;

import com.kinoicon.api.model.response.value.ImageWithNoteResponse;
import com.kinoicon.api.model.response.value.TrailerResponse;

import java.util.List;

public record FilmMediaResponse(String cover_url, List<ImageWithNoteResponse> images, List<TrailerResponse> trailers) {
}
