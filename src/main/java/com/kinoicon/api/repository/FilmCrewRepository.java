package com.kinoicon.api.repository;

import com.kinoicon.api.model.entity.FilmCrewEntity;

import java.util.List;

/** The single M:N relation in the project: a person's participation in a film. */
public interface FilmCrewRepository {
    List<FilmCrewEntity> findByFilmId(long filmId);
    List<FilmCrewEntity> findByPersonId(long personId);
    void replaceForFilm(long filmId, List<FilmCrewEntity> crew);
}
