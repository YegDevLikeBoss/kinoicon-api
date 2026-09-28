package com.kinoicon.api.model.request;

import java.util.List;

public record FeaturedRequest(String title, List<FeaturedRowRequest> featured) {}
