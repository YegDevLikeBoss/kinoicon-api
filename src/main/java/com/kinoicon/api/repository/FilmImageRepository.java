package com.kinoicon.api.repository;

import com.kinoicon.api.model.entity.FilmImageEntity;

import java.util.List;

public interface FilmImageRepository {
    List<FilmImageEntity> findByFilmId(long filmId);
    void replaceForFilm(long filmId, List<FilmImageEntity> images);
}
