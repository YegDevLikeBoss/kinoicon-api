package com.kinoicon.api.model.entity;

import com.kinoicon.api.model.entity.value.External;
import com.kinoicon.api.model.entity.value.Gross;
import com.kinoicon.api.model.entity.value.Logline;
import com.kinoicon.api.model.entity.value.Metadata;
import com.kinoicon.api.model.entity.value.Name;
import com.kinoicon.api.model.entity.value.Price;
import com.kinoicon.api.model.entity.value.Rating;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Nested (variant B) entity: mirrors the API/DTO shape rather than the flat table columns.
 * The RowMapper is responsible for assembling these nested value objects out of the flat
 * columns coming back from PostgreSQL; the Mapper (Entity <-> DTO) stays a trivial 1:1 walk.
 */
@Data
public class FilmEntity {
    private Long id;
    private UUID uuid;

    private Name name;
    private LocalDate date;
    private Integer duration;
    private Logline logline;
    private Price budget;
    private Gross gross;
    private External external;
    private Rating rating;
    private FilmMediaEntity media;
    private Metadata metadata;

    // Loaded separately by the repository (JDBC joins/queries), not stored as JSON.
    private List<FilmCrewEntity> crew;
}
