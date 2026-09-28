package com.kinoicon.api.repository.impl;

import com.kinoicon.api.model.entity.FilmEntity;
import com.kinoicon.api.model.entity.value.External;
import com.kinoicon.api.model.entity.value.Gross;
import com.kinoicon.api.model.entity.value.Logline;
import com.kinoicon.api.model.entity.value.Name;
import com.kinoicon.api.model.entity.value.Price;
import com.kinoicon.api.model.rowmapper.FilmRowMapper;
import com.kinoicon.api.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FilmRepositoryImpl implements FilmRepository {

    private static final String SELECT_COLUMNS = """
            id, uuid, name_en, name_native, name_ru, date, duration,
            logline_en, logline_native, logline_ru,
            budget_amount, budget_currency,
            gross_russia_amount, gross_russia_currency,
            gross_usa_amount, gross_usa_currency,
            gross_world_amount, gross_world_currency,
            external_imdb_id, external_kp_id,
            rating_imdb_rating, rating_imdb_vote_count,
            rating_kp_rating, rating_kp_vote_count,
            rating_metacritic, rating_rottentomatos,
            cover_url, published, schema_version
            """;

    private static final FilmRowMapper ROW_MAPPER = new FilmRowMapper();

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<FilmEntity> findById(long id, boolean includeUnpublished) {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM film WHERE id = ? " + (includeUnpublished ? "" : "AND published = true");
        List<FilmEntity> result = jdbcTemplate.query(sql, ROW_MAPPER, id);
        return result.stream().findFirst();
    }

    @Override
    public Long create() {
        return jdbcTemplate.queryForObject(
                "INSERT INTO film (name_en, name_native, name_ru, published, schema_version) VALUES (NULL, NULL, NULL, false, 1) RETURNING id",
                Long.class);
    }

    @Override
    public boolean update(FilmEntity entity) {
        Name name = entity.getName() != null ? entity.getName() : new Name(null, null, null);
        Logline logline = entity.getLogline() != null ? entity.getLogline() : new Logline(null, null, null);
        Price budget = entity.getBudget();
        Gross gross = entity.getGross() != null ? entity.getGross() : new Gross(null, null, null);
        External external = entity.getExternal();

        int updated = jdbcTemplate.update("""
                UPDATE film SET
                    name_en = ?, name_native = ?, name_ru = ?,
                    date = ?, duration = ?,
                    logline_en = ?, logline_native = ?, logline_ru = ?,
                    budget_amount = ?, budget_currency = ?,
                    gross_russia_amount = ?, gross_russia_currency = ?,
                    gross_usa_amount = ?, gross_usa_currency = ?,
                    gross_world_amount = ?, gross_world_currency = ?,
                    external_imdb_id = ?, external_kp_id = ?,
                    cover_url = ?, published = ?
                WHERE id = ?
                """,
                name.getEn(), name.getNative_(), name.getRu(),
                entity.getDate(), entity.getDuration(),
                logline.getEn(), logline.getNative_(), logline.getRu(),
                budget != null ? budget.getAmount() : null, budget != null ? budget.getCurrency() : null,
                priceAmount(gross.getRussia()), priceCurrency(gross.getRussia()),
                priceAmount(gross.getUsa()), priceCurrency(gross.getUsa()),
                priceAmount(gross.getWorld()), priceCurrency(gross.getWorld()),
                external != null ? external.getImdbId() : null, external != null ? external.getKpId() : null,
                entity.getMedia().getCoverUrl(), entity.getMetadata().isPublished(),
                entity.getId());
        return updated > 0;
    }

    /** Called after the KP rating sync fetches fresh numbers - updates only the rating columns. */
    @Override
    public boolean updateRating(long filmId, String kpRating, int kpVoteCount, String imdbRating, int imdbVoteCount) {
        int updated = jdbcTemplate.update("""
                UPDATE film SET
                    rating_kp_rating = ?, rating_kp_vote_count = ?,
                    rating_imdb_rating = ?, rating_imdb_vote_count = ?
                WHERE id = ?
                """, kpRating, kpVoteCount, imdbRating, imdbVoteCount, filmId);
        return updated > 0;
    }

    @Override
    public boolean deleteById(long id) {
        return jdbcTemplate.update("DELETE FROM film WHERE id = ?", id) > 0;
    }

    @Override
    public List<FilmEntity> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        String placeholders = String.join(",", ids.stream().map(i -> "?").toList());
        String sql = "SELECT " + SELECT_COLUMNS + " FROM film WHERE id IN (" + placeholders + ")";
        return jdbcTemplate.query(sql, ROW_MAPPER, ids.toArray());
    }

    @Override
    public List<com.kinoicon.api.repository.ScoredFilm> search(String query, boolean includeUnpublished, double threshold, int limit) {
        String sql = "SELECT " + SELECT_COLUMNS + ", similarity(search_text, ?) AS score FROM film " +
                "WHERE (? = true OR published = true) AND similarity(search_text, ?) > ? " +
                "ORDER BY score DESC LIMIT ?";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new com.kinoicon.api.repository.ScoredFilm(ROW_MAPPER.mapRow(rs, rowNum), rs.getDouble("score")),
                query, includeUnpublished, query, threshold, limit);
    }

    private Integer priceAmount(Price price) {
        return price != null ? price.getAmount() : null;
    }

    private String priceCurrency(Price price) {
        return price != null ? price.getCurrency() : null;
    }
}
