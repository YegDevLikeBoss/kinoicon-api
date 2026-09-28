package com.kinoicon.api.model.response;

import com.kinoicon.api.model.response.value.NameResponse;

public record PersonMinimalResponse(String id, Long shortId, NameResponse name, String coverUrl) {}
