package com.kinoicon.api.config;

import com.kinoicon.api.model.entity.UserEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Request-scoped holder for whatever the JwtAuthFilter resolved from the Authorization
 * header on this request. Nothing here is persisted anywhere - purely in-memory for the
 * lifetime of a single HTTP request. There is no session object of any kind in the DB.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS)
@Data
public class AuthContext {
    private UserEntity currentUser;
    private boolean tokenExpired;
    private boolean tokenInvalid;

    public boolean isAuthenticated() {
        return currentUser != null;
    }
}
