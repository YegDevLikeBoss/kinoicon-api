package com.kinoicon.api.model.response;

import java.util.List;

public record FeaturedRowResponse(String kind, String title, List<FeaturedItemResponse> items) {}
