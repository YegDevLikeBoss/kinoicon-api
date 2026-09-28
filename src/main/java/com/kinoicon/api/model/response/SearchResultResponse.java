package com.kinoicon.api.model.response;

/** Polymorphic "best" match: kind is either "film" or "person", data holds the matching minimal DTO. */
public record SearchResultResponse(String kind, Object data) {}
