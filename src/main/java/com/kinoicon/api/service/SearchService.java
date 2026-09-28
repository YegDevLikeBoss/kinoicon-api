package com.kinoicon.api.service;

import com.kinoicon.api.model.response.SearchResponse;

public interface SearchService {
    SearchResponse search(String query, boolean authenticated);
}
