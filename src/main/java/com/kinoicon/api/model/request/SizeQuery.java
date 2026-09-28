package com.kinoicon.api.model.request;

/** GET ?size= query param - allowed values validated against SizeType enum in the service layer. */
public record SizeQuery(String size) {}
