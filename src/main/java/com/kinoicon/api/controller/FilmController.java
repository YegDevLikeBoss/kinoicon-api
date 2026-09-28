package com.kinoicon.api.controller;

import com.kinoicon.api.config.AuthContext;
import com.kinoicon.api.config.RequireAuth;
import com.kinoicon.api.exception.BaseException;
import com.kinoicon.api.model.enums.SizeType;
import com.kinoicon.api.model.request.FilmRequest;
import com.kinoicon.api.model.response.IdResponse;
import com.kinoicon.api.model.response.MessageResponse;
import com.kinoicon.api.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "film")
public class FilmController {

    private final FilmService filmService;
    private final AuthContext authContext;

    @GetMapping("/film/{filmId}")
    @Operation(summary = "Returns film object with selected id")
    public ResponseEntity<Object> get(@PathVariable Long filmId,
                                       @RequestParam(name = "size", defaultValue = "full") String size) {
        SizeType sizeType = SizeType.fromValue(size).orElseThrow(() -> BaseException.unknownUrlArgValues(size));
        boolean authenticated = authContext.isAuthenticated();
        Object film = filmService.getById(filmId, sizeType, authenticated);
        return ResponseEntity.ok(film);
    }

    @PutMapping("/film/{filmId}")
    @RequireAuth
    @Operation(summary = "Updates film object with selected id")
    public ResponseEntity<MessageResponse> put(@PathVariable Long filmId, @Valid @RequestBody FilmRequest request) {
        filmService.update(filmId, request);
        return ResponseEntity.ok(new MessageResponse("Successfully updated"));
    }

    @PostMapping("/film")
    @RequireAuth
    @Operation(summary = "Creates a film and returns id")
    public ResponseEntity<IdResponse> post() {
        long id = filmService.create();
        return ResponseEntity.status(HttpStatus.CREATED).body(new IdResponse(id));
    }

    @DeleteMapping("/film/{filmId}")
    @RequireAuth
    @Operation(summary = "Deletes a film object with selected id")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long filmId) {
        filmService.delete(filmId);
        return ResponseEntity.ok(new MessageResponse("Successfully deleted"));
    }
}
