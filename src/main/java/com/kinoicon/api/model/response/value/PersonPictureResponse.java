package com.kinoicon.api.model.response.value;

import java.util.List;

public record PersonPictureResponse(FilmMinimalResponse picture, List<String> role, String note) {}
