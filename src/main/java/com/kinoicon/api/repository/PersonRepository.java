package com.kinoicon.api.repository;

import com.kinoicon.api.model.entity.PersonEntity;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {
    Optional<PersonEntity> findById(long id, boolean includeUnpublished);
    Long create();
    boolean update(PersonEntity entity);
    boolean deleteById(long id);
    List<PersonEntity> findByIds(List<Long> ids);
    List<PersonEntity> findByUuids(List<String> uuids);
    List<ScoredPerson> search(String query, boolean includeUnpublished, double threshold, int limit);
}
