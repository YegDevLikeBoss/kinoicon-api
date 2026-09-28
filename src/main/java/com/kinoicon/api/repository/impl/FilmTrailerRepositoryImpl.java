package com.kinoicon.api.repository.impl;

import com.kinoicon.api.model.entity.FilmTrailerEntity;
import com.kinoicon.api.model.rowmapper.FilmTrailerRowMapper;
import com.kinoicon.api.repository.FilmTrailerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilmTrailerRepositoryImpl implements FilmTrailerRepository {

    private static final FilmTrailerRowMapper ROW_MAPPER = new FilmTrailerRowMapper();

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<FilmTrailerEntity> findByFilmId(long filmId) {
        return jdbcTemplate.query("SELECT id, film_id, language, title, video_url FROM film_trailer WHERE film_id = ?", ROW_MAPPER, filmId);
    }

    @Override
    @Transactional
    public void replaceForFilm(long filmId, List<FilmTrailerEntity> trailers) {
        jdbcTemplate.update("DELETE FROM film_trailer WHERE film_id = ?", filmId);
        if (trailers == null) {
            return;
        }
        for (FilmTrailerEntity trailer : trailers) {
            jdbcTemplate.update("INSERT INTO film_trailer (film_id, language, title, video_url) VALUES (?, ?, ?, ?)",
                    filmId, trailer.getLanguage(), trailer.getTitle(), trailer.getVideoUrl());
        }
    }
}
