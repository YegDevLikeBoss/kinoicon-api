package com.kinoicon.api.model.rowmapper;

import com.kinoicon.api.model.entity.FilmEntity;
import com.kinoicon.api.model.entity.FilmMediaEntity;
import com.kinoicon.api.model.entity.value.External;
import com.kinoicon.api.model.entity.value.Gross;
import com.kinoicon.api.model.entity.value.Logline;
import com.kinoicon.api.model.entity.value.Metadata;
import com.kinoicon.api.model.entity.value.Name;
import com.kinoicon.api.model.entity.value.Price;
import com.kinoicon.api.model.entity.value.Rating;
import com.kinoicon.api.model.entity.value.RatingItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Assembles the nested FilmEntity out of the flat "film" table columns.
 * All of the "flatten <-> nest" complexity lives here, deliberately - the Entity<->DTO
 * mapper downstream stays a trivial field-by-field walk.
 * Does not populate images/trailers/crew - those are loaded by separate repository
 * queries and stitched together by the service layer.
 */
public class FilmRowMapper implements RowMapper<FilmEntity> {

    @Override
    public FilmEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmEntity entity = new FilmEntity();
        entity.setId(rs.getLong("id"));
        entity.setUuid(UUID.fromString(rs.getString("uuid")));

        entity.setName(new Name(
                rs.getString("name_en"),
                rs.getString("name_native"),
                rs.getString("name_ru")
        ));

        entity.setDate(rs.getObject("date", java.time.LocalDate.class));
        entity.setDuration((Integer) rs.getObject("duration"));

        entity.setLogline(new Logline(
                rs.getString("logline_en"),
                rs.getString("logline_native"),
                rs.getString("logline_ru")
        ));

        entity.setBudget(rs.getObject("budget_amount") == null ? null : new Price(
                (Integer) rs.getObject("budget_amount"),
                rs.getString("budget_currency")
        ));

        entity.setGross(new Gross(
                priceOrNull(rs, "gross_russia_amount", "gross_russia_currency"),
                priceOrNull(rs, "gross_usa_amount", "gross_usa_currency"),
                priceOrNull(rs, "gross_world_amount", "gross_world_currency")
        ));

        String imdbId = rs.getString("external_imdb_id");
        String kpId = rs.getString("external_kp_id");
        entity.setExternal(imdbId == null && kpId == null ? null : new External(imdbId, kpId));

        RatingItem imdbRating = ratingItemOrNull(rs, "rating_imdb_rating", "rating_imdb_vote_count");
        RatingItem kpRating = ratingItemOrNull(rs, "rating_kp_rating", "rating_kp_vote_count");
        Integer metacritic = (Integer) rs.getObject("rating_metacritic");
        Double rottentomatos = (Double) rs.getObject("rating_rottentomatos");
        entity.setRating(new Rating(imdbRating, kpRating, metacritic, rottentomatos));

        FilmMediaEntity media = new FilmMediaEntity(rs.getString("cover_url"), null, null);
        entity.setMedia(media);

        entity.setMetadata(new Metadata(
                rs.getBoolean("published"),
                rs.getInt("schema_version")
        ));

        return entity;
    }

    private Price priceOrNull(ResultSet rs, String amountCol, String currencyCol) throws SQLException {
        Object amount = rs.getObject(amountCol);
        if (amount == null) {
            return null;
        }
        return new Price((Integer) amount, rs.getString(currencyCol));
    }

    private RatingItem ratingItemOrNull(ResultSet rs, String ratingCol, String voteCountCol) throws SQLException {
        String rating = rs.getString(ratingCol);
        if (rating == null) {
            return null;
        }
        return new RatingItem(rating, (Integer) rs.getObject(voteCountCol));
    }
}
