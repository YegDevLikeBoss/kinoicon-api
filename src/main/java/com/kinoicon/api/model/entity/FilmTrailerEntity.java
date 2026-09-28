package com.kinoicon.api.model.entity;

@lombok.Data
public class FilmTrailerEntity {
    private Long id;
    private Long filmId;
    private String language;
    private String title;
    private String videoUrl;
}
