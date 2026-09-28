package com.kinoicon.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeaturedItemRequest(@NotBlank String kind, @NotNull Long short_id) {}
