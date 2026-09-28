package com.kinoicon.api.config;

import com.kinoicon.api.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Purely stateless JWT resolution - no session object of any kind is stored anywhere.
 * On every request, if an Authorization: Bearer header is present, the token is validated
 * and the user re-loaded from the DB by username; the result is stashed on the
 * request-scoped AuthContext for controllers/interceptors to read. A missing header is not
 * an error here - endpoints decide for themselves (via @RequireAuth) whether a user is
 * mandatory.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthContext authContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            String token = header.substring(BEARER_PREFIX.length());
            try {
                if (jwtService.isExpired(token)) {
                    authContext.setTokenExpired(true);
                } else {
                    String username = jwtService.extractUsername(token);
                    userRepository.findByUsername(username).ifPresentOrElse(
                            authContext::setCurrentUser,
                            () -> authContext.setTokenInvalid(true));
                }
            } catch (Exception e) {
                authContext.setTokenInvalid(true);
            }
        }

        filterChain.doFilter(request, response);
    }
}
