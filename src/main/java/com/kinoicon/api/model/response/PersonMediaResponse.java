package com.kinoicon.api.model.response;

import com.kinoicon.api.model.response.value.ImageWithNoteResponse;

import java.util.List;

public record PersonMediaResponse(String cover_url, List<ImageWithNoteResponse> images) {
}
