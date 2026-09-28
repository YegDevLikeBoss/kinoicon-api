package com.kinoicon.api.repository;

import com.kinoicon.api.model.entity.FilmEntity;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    Optional<FilmEntity> findById(long id, boolean includeUnpublished);
    Long create();
    boolean update(FilmEntity entity);
    boolean updateRating(long filmId, String kpRating, int kpVoteCount, String imdbRating, int imdbVoteCount);
    boolean deleteById(long id);
    List<FilmEntity> findByIds(List<Long> ids);
    List<ScoredFilm> search(String query, boolean includeUnpublished, double threshold, int limit);
}
