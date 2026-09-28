package com.kinoicon.api.model.rowmapper;

import com.kinoicon.api.model.entity.FeaturedRowItemEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FeaturedRowItemRowMapper implements RowMapper<FeaturedRowItemEntity> {
    @Override
    public FeaturedRowItemEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FeaturedRowItemEntity entity = new FeaturedRowItemEntity();
        entity.setId(rs.getLong("id"));
        entity.setRowId(rs.getLong("row_id"));
        entity.setPosition(rs.getInt("position"));
        entity.setItemKind(rs.getString("item_kind"));
        entity.setItemShortId(rs.getLong("item_short_id"));
        return entity;
    }
}
