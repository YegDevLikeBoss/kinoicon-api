package com.kinoicon.api.model.request.value;

import jakarta.validation.constraints.NotBlank;

public record TrailerRequest(String language, String title, @NotBlank String video_url) {}
