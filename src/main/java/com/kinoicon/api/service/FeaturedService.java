package com.kinoicon.api.service;

import com.kinoicon.api.model.request.FeaturedRequest;
import com.kinoicon.api.model.response.FeaturedResponse;

public interface FeaturedService {
    FeaturedResponse get();
    void update(FeaturedRequest request);
}
