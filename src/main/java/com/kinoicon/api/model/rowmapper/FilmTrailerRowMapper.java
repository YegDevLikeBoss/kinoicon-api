package com.kinoicon.api.model.rowmapper;

import com.kinoicon.api.model.entity.FilmTrailerEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmTrailerRowMapper implements RowMapper<FilmTrailerEntity> {
    @Override
    public FilmTrailerEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmTrailerEntity entity = new FilmTrailerEntity();
        entity.setId(rs.getLong("id"));
        entity.setFilmId(rs.getLong("film_id"));
        entity.setLanguage(rs.getString("language"));
        entity.setTitle(rs.getString("title"));
        entity.setVideoUrl(rs.getString("video_url"));
        return entity;
    }
}
