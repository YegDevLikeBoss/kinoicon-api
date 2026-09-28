package com.kinoicon.api.model.entity;

@lombok.Data
public class FilmImageEntity {
    private Long id;
    private Long filmId;
    private String imageUrl;
    private String comment;
    private String tag;
}
