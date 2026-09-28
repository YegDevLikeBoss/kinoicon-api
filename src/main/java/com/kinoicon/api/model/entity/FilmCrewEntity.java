package com.kinoicon.api.model.entity;

import lombok.Data;

import java.util.List;

/**
 * The single many-to-many relation in the project: a person's participation in a film.
 * Flat junction entity - no nested value objects, since it has no identity of its own
 * beyond linking film <-> person with role attributes on the edge.
 */
@Data
public class FilmCrewEntity {
    private Long id;
    private Long filmId;
    private Long personId;
    private boolean isLead;
    private List<String> leadingRoles;
    private List<String> otherRoles;
    private String note;
}
