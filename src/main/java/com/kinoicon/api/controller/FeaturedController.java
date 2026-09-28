package com.kinoicon.api.controller;

import com.kinoicon.api.config.RequireAuth;
import com.kinoicon.api.model.request.FeaturedRequest;
import com.kinoicon.api.model.response.FeaturedResponse;
import com.kinoicon.api.model.response.MessageResponse;
import com.kinoicon.api.service.FeaturedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "featured")
public class FeaturedController {

    private final FeaturedService featuredService;

    @GetMapping("/")
    @Operation(summary = "Returns featured films and actors data")
    public ResponseEntity<FeaturedResponse> get() {
        return ResponseEntity.ok(featuredService.get());
    }

    @PutMapping("/")
    @RequireAuth
    @Operation(summary = "Updates featured films and actors data")
    public ResponseEntity<MessageResponse> put(@Valid @RequestBody FeaturedRequest request) {
        featuredService.update(request);
        return ResponseEntity.ok(new MessageResponse("Successfully updated"));
    }
}
