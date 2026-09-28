package com.kinoicon.api.model.rowmapper;

import com.kinoicon.api.model.entity.FeaturedRowEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/** Maps a single featured_row row. The `items` list is populated separately by the repository. */
public class FeaturedRowRowMapper implements RowMapper<FeaturedRowEntity> {
    @Override
    public FeaturedRowEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FeaturedRowEntity entity = new FeaturedRowEntity();
        entity.setId(rs.getLong("id"));
        entity.setFeaturedId(rs.getLong("featured_id"));
        entity.setPosition(rs.getInt("position"));
        entity.setKind(rs.getString("kind"));
        entity.setTitle(rs.getString("title"));
        return entity;
    }
}
