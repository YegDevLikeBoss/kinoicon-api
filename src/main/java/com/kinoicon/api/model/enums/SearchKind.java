package com.kinoicon.api.model.enums;

/** Discriminator used in Search.best and Featured row items - must match the API contract. */
public enum SearchKind {
    FILM("film"),
    PERSON("person");

    private final String value;

    SearchKind(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
