package com.kinoicon.api.repository.impl;

import com.kinoicon.api.model.entity.FilmImageEntity;
import com.kinoicon.api.model.rowmapper.FilmImageRowMapper;
import com.kinoicon.api.repository.FilmImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilmImageRepositoryImpl implements FilmImageRepository {

    private static final FilmImageRowMapper ROW_MAPPER = new FilmImageRowMapper();

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<FilmImageEntity> findByFilmId(long filmId) {
        return jdbcTemplate.query("SELECT id, film_id, image_url, comment, tag FROM film_image WHERE film_id = ?", ROW_MAPPER, filmId);
    }

    @Override
    @Transactional
    public void replaceForFilm(long filmId, List<FilmImageEntity> images) {
        jdbcTemplate.update("DELETE FROM film_image WHERE film_id = ?", filmId);
        if (images == null) {
            return;
        }
        for (FilmImageEntity image : images) {
            jdbcTemplate.update("INSERT INTO film_image (film_id, image_url, comment, tag) VALUES (?, ?, ?, ?)",
                    filmId, image.getImageUrl(), image.getComment(), image.getTag());
        }
    }
}
