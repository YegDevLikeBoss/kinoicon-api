package com.kinoicon.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FilmMediaEntity {
    private String coverUrl;

    // Loaded separately by the repository (JDBC joins/queries), not stored as JSON.
    private List<FilmImageEntity> images;
    private List<FilmTrailerEntity> trailers;
}
