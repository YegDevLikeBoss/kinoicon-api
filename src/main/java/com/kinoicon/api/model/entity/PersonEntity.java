package com.kinoicon.api.model.entity;

import com.kinoicon.api.model.entity.value.DateAndPlace;
import com.kinoicon.api.model.entity.value.Metadata;
import com.kinoicon.api.model.entity.value.Name;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PersonEntity {
    private Long id;
    private UUID uuid;

    private Name name;
    private DateAndPlace born;
    private DateAndPlace died; // nullable
    private Integer length;
    private PersonMediaEntity media;
    private Metadata metadata;

    // Loaded separately by the repository: this person's participation across films.
    private List<FilmCrewEntity> crew;
}
