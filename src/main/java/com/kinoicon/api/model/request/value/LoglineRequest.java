package com.kinoicon.api.model.request.value;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoglineRequest(String en, @JsonProperty("native") String native_, String ru) {}
