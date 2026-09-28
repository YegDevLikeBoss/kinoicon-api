package com.kinoicon.api.model.rowmapper;

import com.kinoicon.api.model.entity.FilmCrewEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class FilmCrewRowMapper implements RowMapper<FilmCrewEntity> {
    @Override
    public FilmCrewEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmCrewEntity entity = new FilmCrewEntity();
        entity.setId(rs.getLong("id"));
        entity.setFilmId(rs.getLong("film_id"));
        entity.setPersonId(rs.getLong("person_id"));
        entity.setLead(rs.getBoolean("is_lead"));
        entity.setLeadingRoles(toList(rs.getArray("leading_roles")));
        entity.setOtherRoles(toList(rs.getArray("other_roles")));
        entity.setNote(rs.getString("note"));
        return entity;
    }

    private List<String> toList(Array array) throws SQLException {
        if (array == null) {
            return null;
        }
        return Arrays.asList((String[]) array.getArray());
    }
}
