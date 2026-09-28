package com.kinoicon.api.repository.impl;

import com.kinoicon.api.model.entity.FilmCrewEntity;
import com.kinoicon.api.model.rowmapper.FilmCrewRowMapper;
import com.kinoicon.api.repository.FilmCrewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilmCrewRepositoryImpl implements FilmCrewRepository {

    private static final FilmCrewRowMapper ROW_MAPPER = new FilmCrewRowMapper();
    private static final String SELECT_COLUMNS = "id, film_id, person_id, is_lead, leading_roles, other_roles, note";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<FilmCrewEntity> findByFilmId(long filmId) {
        return jdbcTemplate.query("SELECT " + SELECT_COLUMNS + " FROM film_crew WHERE film_id = ?", ROW_MAPPER, filmId);
    }

    @Override
    public List<FilmCrewEntity> findByPersonId(long personId) {
        return jdbcTemplate.query("SELECT " + SELECT_COLUMNS + " FROM film_crew WHERE person_id = ?", ROW_MAPPER, personId);
    }

    @Override
    @Transactional
    public void replaceForFilm(long filmId, List<FilmCrewEntity> crew) {
        jdbcTemplate.update("DELETE FROM film_crew WHERE film_id = ?", filmId);
        if (crew == null) {
            return;
        }
        for (FilmCrewEntity c : crew) {
            jdbcTemplate.update(con -> {
                var ps = con.prepareStatement(
                        "INSERT INTO film_crew (film_id, person_id, is_lead, leading_roles, other_roles, note) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setLong(1, filmId);
                ps.setLong(2, c.getPersonId());
                ps.setBoolean(3, c.isLead());
                ps.setArray(4, c.getLeadingRoles() == null ? null : con.createArrayOf("text", c.getLeadingRoles().toArray()));
                ps.setArray(5, c.getOtherRoles() == null ? null : con.createArrayOf("text", c.getOtherRoles().toArray()));
                ps.setString(6, c.getNote());
                return ps;
            });
        }
    }
}
