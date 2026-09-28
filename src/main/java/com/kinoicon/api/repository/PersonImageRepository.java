package com.kinoicon.api.repository;

import com.kinoicon.api.model.entity.PersonImageEntity;

import java.util.List;

public interface PersonImageRepository {
    List<PersonImageEntity> findByPersonId(long personId);
    void replaceForPerson(long personId, List<PersonImageEntity> images);
}
