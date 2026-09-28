package com.kinoicon.api.controller;

import com.kinoicon.api.config.AuthContext;
import com.kinoicon.api.config.RequireAuth;
import com.kinoicon.api.exception.BaseException;
import com.kinoicon.api.model.enums.SizeType;
import com.kinoicon.api.model.request.PersonRequest;
import com.kinoicon.api.model.response.IdResponse;
import com.kinoicon.api.model.response.MessageResponse;
import com.kinoicon.api.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "person")
public class PersonController {

    private final PersonService personService;
    private final AuthContext authContext;

    @GetMapping("/person/{personId}")
    @Operation(summary = "Returns person object with selected id")
    public ResponseEntity<Object> get(@PathVariable Long personId,
                                       @RequestParam(name = "size", defaultValue = "full") String size) {
        SizeType sizeType = SizeType.fromValue(size).orElseThrow(() -> BaseException.unknownUrlArgValues(size));
        boolean authenticated = authContext.isAuthenticated();
        Object person = personService.getById(personId, sizeType, authenticated);
        return ResponseEntity.ok(person);
    }

    @PutMapping("/person/{personId}")
    @RequireAuth
    @Operation(summary = "Updates person object with selected id")
    public ResponseEntity<MessageResponse> put(@PathVariable Long personId, @Valid @RequestBody PersonRequest request) {
        personService.update(personId, request);
        return ResponseEntity.ok(new MessageResponse("Successfully updated"));
    }

    @PostMapping("/person")
    @RequireAuth
    @Operation(summary = "Creates a person and returns id")
    public ResponseEntity<IdResponse> post() {
        long id = personService.create();
        return ResponseEntity.status(HttpStatus.CREATED).body(new IdResponse(id));
    }

    @DeleteMapping("/person/{personId}")
    @RequireAuth
    @Operation(summary = "Deletes a person object with selected id")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long personId) {
        personService.delete(personId);
        return ResponseEntity.ok(new MessageResponse("Successfully deleted"));
    }
}
