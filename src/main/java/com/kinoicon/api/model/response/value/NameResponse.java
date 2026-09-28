package com.kinoicon.api.model.response.value;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NameResponse(String en, @JsonProperty("native") String native_, String ru) {}
