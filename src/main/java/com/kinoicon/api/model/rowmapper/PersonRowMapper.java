package com.kinoicon.api.model.rowmapper;

import com.kinoicon.api.model.entity.PersonEntity;
import com.kinoicon.api.model.entity.PersonMediaEntity;
import com.kinoicon.api.model.entity.value.DateAndPlace;
import com.kinoicon.api.model.entity.value.Metadata;
import com.kinoicon.api.model.entity.value.Name;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PersonRowMapper implements RowMapper<PersonEntity> {

    @Override
    public PersonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        PersonEntity entity = new PersonEntity();
        entity.setId(rs.getLong("id"));
        entity.setUuid(UUID.fromString(rs.getString("uuid")));

        entity.setName(new Name(
                rs.getString("name_en"),
                rs.getString("name_native"),
                rs.getString("name_ru")
        ));

        entity.setBorn(new DateAndPlace(
                rs.getString("born_city"),
                rs.getObject("born_date", java.time.LocalDate.class)
        ));

        String diedCity = rs.getString("died_city");
        java.time.LocalDate diedDate = rs.getObject("died_date", java.time.LocalDate.class);
        entity.setDied(diedCity == null && diedDate == null ? null : new DateAndPlace(diedCity, diedDate));

        entity.setLength((Integer) rs.getObject("length"));

        PersonMediaEntity media = new PersonMediaEntity(rs.getString("cover_url"), null);
        entity.setMedia(media);

        entity.setMetadata(new Metadata(
                rs.getBoolean("published"),
                rs.getInt("schema_version")
        ));

        return entity;
    }
}
