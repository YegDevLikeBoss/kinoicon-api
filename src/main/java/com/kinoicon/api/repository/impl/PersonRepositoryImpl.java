package com.kinoicon.api.repository.impl;

import com.kinoicon.api.model.entity.PersonEntity;
import com.kinoicon.api.model.entity.value.DateAndPlace;
import com.kinoicon.api.model.entity.value.Name;
import com.kinoicon.api.model.rowmapper.PersonRowMapper;
import com.kinoicon.api.repository.PersonRepository;
import com.kinoicon.api.repository.ScoredPerson;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PersonRepositoryImpl implements PersonRepository {

    private static final String SELECT_COLUMNS = """
            id, uuid, name_en, name_native, name_ru,
            born_city, born_date, died_city, died_date,
            length, cover_url, published, schema_version
            """;

    private static final PersonRowMapper ROW_MAPPER = new PersonRowMapper();

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<PersonEntity> findById(long id, boolean includeUnpublished) {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM person WHERE id = ? " + (includeUnpublished ? "" : "AND published = true");
        List<PersonEntity> result = jdbcTemplate.query(sql, ROW_MAPPER, id);
        return result.stream().findFirst();
    }

    @Override
    public Long create() {
        return jdbcTemplate.queryForObject(
                "INSERT INTO person (name_en, published, schema_version) VALUES (NULL, false, 1) RETURNING id",
                Long.class);
    }

    @Override
    public boolean update(PersonEntity entity) {
        Name name = entity.getName() != null ? entity.getName() : new Name(null, null, null);
        DateAndPlace born = entity.getBorn() != null ? entity.getBorn() : new DateAndPlace(null, null);
        DateAndPlace died = entity.getDied();

        int updated = jdbcTemplate.update("""
                UPDATE person SET
                    name_en = ?, name_native = ?, name_ru = ?,
                    born_city = ?, born_date = ?,
                    died_city = ?, died_date = ?,
                    length = ?, cover_url = ?, published = ?
                WHERE id = ?
                """,
                name.getEn(), name.getNative_(), name.getRu(),
                born.getCity(), born.getDate(),
                died != null ? died.getCity() : null, died != null ? died.getDate() : null,
                entity.getLength(), entity.getMedia().getCoverUrl(), entity.getMetadata().isPublished(),
                entity.getId());
        return updated > 0;
    }

    @Override
    public boolean deleteById(long id) {
        return jdbcTemplate.update("DELETE FROM person WHERE id = ?", id) > 0;
    }

    @Override
    public List<PersonEntity> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        String placeholders = String.join(",", ids.stream().map(i -> "?").toList());
        String sql = "SELECT " + SELECT_COLUMNS + " FROM person WHERE id IN (" + placeholders + ")";
        return jdbcTemplate.query(sql, ROW_MAPPER, ids.toArray());
    }

    @Override
    public List<PersonEntity> findByUuids(List<String> uuids) {
        if (uuids == null || uuids.isEmpty()) {
            return List.of();
        }
        String placeholders = String.join(",", uuids.stream().map(i -> "?").toList());
        String sql = "SELECT " + SELECT_COLUMNS + " FROM person WHERE uuid IN (" + placeholders + ")";

        Object[] params = uuids.stream().map(java.util.UUID::fromString).toArray();
        return jdbcTemplate.query(sql, ROW_MAPPER, params);
    }

    @Override
    public List<ScoredPerson> search(String query, boolean includeUnpublished, double threshold, int limit) {
        String sql = "SELECT " + SELECT_COLUMNS + ", similarity(search_text, ?) AS score FROM person " +
                "WHERE (? = true OR published = true) AND similarity(search_text, ?) > ? " +
                "ORDER BY score DESC LIMIT ?";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new ScoredPerson(ROW_MAPPER.mapRow(rs, rowNum), rs.getDouble("score")),
                query, includeUnpublished, query, threshold, limit);
    }
}
