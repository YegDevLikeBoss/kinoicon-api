package com.kinoicon.api.repository;

import com.kinoicon.api.model.entity.PersonEntity;

public record ScoredPerson(PersonEntity entity, double score) {}
