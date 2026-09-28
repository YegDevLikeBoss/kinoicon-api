package com.kinoicon.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class BaseException extends RuntimeException {

    private final int errorCode;
    private final String moreInfo;
    private final transient Object usefullData;
    private final HttpStatus httpStatus;

    private BaseException(int errorCode, String message, String moreInfo, Object usefullData, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.moreInfo = moreInfo;
        this.usefullData = usefullData;
        this.httpStatus = httpStatus;
    }

    public static BaseException wrongUserCredentials() {
        return new BaseException(40000, "Wrong username or password", null, null, HttpStatus.UNAUTHORIZED);
    }

    public static BaseException userNotFound() {
        return new BaseException(40000, "User not found", null, null, HttpStatus.NOT_FOUND);
    }

    public static BaseException userAlreadyRegistered() {
        return new BaseException(40000, "User already registered", null, null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public static BaseException unknownError(Object data) {
        Object payload = data != null ? Map.of("error", String.valueOf(data)) : null;
        return new BaseException(50000, "Error happened on server side. It's not your fault", null, payload, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static BaseException filmNotFound() {
        return new BaseException(40000, "Film not found", null, null, HttpStatus.NOT_FOUND);
    }

    public static BaseException personNotFound() {
        return new BaseException(40000, "Person not found", null, null, HttpStatus.NOT_FOUND);
    }

    public static BaseException featuredNotFound() {
        return new BaseException(40000, "Featured not found", null, null, HttpStatus.NOT_FOUND);
    }

    public static BaseException unknownUrlArgs(Object data) {
        return new BaseException(40000, "Provided URL args are invalid. See invalid args in 'usefull_data'", null, data, HttpStatus.BAD_REQUEST);
    }

    public static BaseException unknownUrlArgValues(Object data) {
        Object payload = Map.of(String.valueOf(data), "Wrong value, try (full, summary, minimal)");
        return new BaseException(40000, "Provided URL arg values are invalid. See invalid values in 'usefull_data'", null, payload, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public static BaseException missingJson(Object message) {
        return new BaseException(40000, "Missing JSON in request", null, message, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    public static BaseException tokenExpired() {
        return new BaseException(40000, "Token has expired. Log in to get new token.", null, null, HttpStatus.UNAUTHORIZED);
    }

    public static BaseException unauthorized() {
        return new BaseException(40000, "Unauthorized", null, null, HttpStatus.UNAUTHORIZED);
    }

    public static BaseException emptySearchQuery() {
        return new BaseException(40000, "Search query must not be empty", null, null, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
