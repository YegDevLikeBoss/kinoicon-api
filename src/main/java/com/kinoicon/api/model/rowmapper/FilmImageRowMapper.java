package com.kinoicon.api.model.rowmapper;

import com.kinoicon.api.model.entity.FilmImageEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmImageRowMapper implements RowMapper<FilmImageEntity> {
    @Override
    public FilmImageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmImageEntity entity = new FilmImageEntity();
        entity.setId(rs.getLong("id"));
        entity.setFilmId(rs.getLong("film_id"));
        entity.setImageUrl(rs.getString("image_url"));
        entity.setComment(rs.getString("comment"));
        entity.setTag(rs.getString("tag"));
        return entity;
    }
}
