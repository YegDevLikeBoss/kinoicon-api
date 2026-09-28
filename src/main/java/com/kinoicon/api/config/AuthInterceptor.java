package com.kinoicon.api.config;

import com.kinoicon.api.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/** Enforces @RequireAuth on controller methods, mirroring Flask's @jwt_required(). */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthContext authContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        RequireAuth requireAuth = handlerMethod.getMethodAnnotation(RequireAuth.class);
        if (requireAuth == null) {
            return true;
        }
        if (!authContext.isAuthenticated()) {
            if (authContext.isTokenExpired()) {
                throw BaseException.tokenExpired();
            }
            throw BaseException.unauthorized();
        }
        return true;
    }
}
