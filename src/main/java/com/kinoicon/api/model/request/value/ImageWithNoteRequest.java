package com.kinoicon.api.model.request.value;

import jakarta.validation.constraints.NotBlank;

public record ImageWithNoteRequest(@NotBlank String image_url, String comment, String tag) {}
