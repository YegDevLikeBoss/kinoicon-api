package com.kinoicon.api.controller;

import com.kinoicon.api.config.RequireAuth;
import com.kinoicon.api.model.request.CredentialsRequest;
import com.kinoicon.api.model.response.MessageResponse;
import com.kinoicon.api.model.response.TokenResponse;
import com.kinoicon.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "user")
public class UserController {

    private final UserService userService;

    /** Registration requires a valid JWT, same as the original Flask endpoint - there is no
     *  open sign-up. The very first admin account must be inserted directly into the DB;
     *  every account after that is created by someone who already holds a token. */
    @PostMapping("/register")
    @RequireAuth
    @Operation(summary = "Register user")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody CredentialsRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("User successfully created"));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody CredentialsRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @GetMapping("/me")
    @RequireAuth
    @Operation(summary = "Check for user existence")
    public ResponseEntity<MessageResponse> me() {
        return ResponseEntity.ok(new MessageResponse("exists"));
    }
}
