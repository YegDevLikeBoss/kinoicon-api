package com.kinoicon.api.model.request.value;

import jakarta.validation.constraints.PositiveOrZero;

public record PriceRequest(@PositiveOrZero Integer amount, String currency) {}
