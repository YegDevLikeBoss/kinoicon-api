package com.kinoicon.api.model.request;

import jakarta.validation.constraints.NotBlank;

public record CredentialsRequest(@NotBlank String username, @NotBlank String password) {}
