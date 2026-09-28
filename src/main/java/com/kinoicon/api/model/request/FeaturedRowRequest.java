package com.kinoicon.api.model.request;

import java.util.List;

public record FeaturedRowRequest(String kind, String title, List<FeaturedItemRequest> items) {}
