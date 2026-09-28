package com.kinoicon.api.model.enums;

import java.util.Arrays;
import java.util.Optional;

/** Values must match the API contract exactly: full, summary, minimal. */
public enum SizeType {
    FULL("full"),
    SUMMARY("summary"),
    MINIMAL("minimal");

    private final String value;

    SizeType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Optional<SizeType> fromValue(String value) {
        return Arrays.stream(values()).filter(v -> v.value.equals(value)).findFirst();
    }
}
