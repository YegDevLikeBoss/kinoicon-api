package com.kinoicon.api.repository;

import com.kinoicon.api.model.entity.FeaturedEntity;

import java.util.Optional;

/** Featured is a singleton aggregate (one row + its nested rows/items), mirroring Featured.objects.first(). */
public interface FeaturedRepository {
    Optional<FeaturedEntity> find();
    void replace(FeaturedEntity entity);
}
