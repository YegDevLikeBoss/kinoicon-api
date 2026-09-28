package com.kinoicon.api.repository;

import com.kinoicon.api.model.entity.FilmEntity;

/** Internal query-result shape carrying the trigram similarity score alongside the entity -
 *  needed only to replicate the original Mongo text-score comparison in SearchService. */
public record ScoredFilm(FilmEntity entity, double score) {}
