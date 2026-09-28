package com.kinoicon.api.controller;

import com.kinoicon.api.config.AuthContext;
import com.kinoicon.api.exception.BaseException;
import com.kinoicon.api.model.response.SearchResponse;
import com.kinoicon.api.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "search")
public class SearchController {

    private final SearchService searchService;
    private final AuthContext authContext;

    @GetMapping("/search")
    @Operation(summary = "Does search query over films and person")
    public ResponseEntity<SearchResponse> get(@RequestParam(name = "query", required = false) String query) {
        if (query == null) {
            throw BaseException.unknownUrlArgs(Map.of("query", "Missing data for required field."));
        }
        boolean authenticated = authContext.isAuthenticated();
        return ResponseEntity.ok(searchService.search(query, authenticated));
    }
}
