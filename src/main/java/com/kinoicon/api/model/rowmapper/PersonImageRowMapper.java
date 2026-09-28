package com.kinoicon.api.model.rowmapper;

import com.kinoicon.api.model.entity.PersonImageEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonImageRowMapper implements RowMapper<PersonImageEntity> {
    @Override
    public PersonImageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        PersonImageEntity entity = new PersonImageEntity();
        entity.setId(rs.getLong("id"));
        entity.setPersonId(rs.getLong("person_id"));
        entity.setImageUrl(rs.getString("image_url"));
        entity.setComment(rs.getString("comment"));
        entity.setTag(rs.getString("tag"));
        return entity;
    }
}
