package com.kinoicon.api.model.request.value;

import java.time.LocalDate;

public record DateAndPlaceRequest(String city, LocalDate date) {}
