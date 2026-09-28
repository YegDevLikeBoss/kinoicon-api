package com.kinoicon.api.repository;

import com.kinoicon.api.model.entity.FilmTrailerEntity;

import java.util.List;

public interface FilmTrailerRepository {
    List<FilmTrailerEntity> findByFilmId(long filmId);
    void replaceForFilm(long filmId, List<FilmTrailerEntity> trailers);
}
