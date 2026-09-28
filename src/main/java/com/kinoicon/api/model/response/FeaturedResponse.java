package com.kinoicon.api.model.response;

import java.util.List;

public record FeaturedResponse(String title, List<FeaturedRowResponse> featured) {}
