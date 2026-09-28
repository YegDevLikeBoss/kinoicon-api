package com.kinoicon.api.model.request;

import jakarta.validation.constraints.NotNull;

public record SearchQuery(@NotNull String query) {}
