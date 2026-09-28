package com.kinoicon.api.model.response.value;

public record RatingResponse(RatingItemResponse imdb, RatingItemResponse kp, Integer metacritic, Double rottentomatos) {}
