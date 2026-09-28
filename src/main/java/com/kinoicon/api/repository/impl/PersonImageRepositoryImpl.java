package com.kinoicon.api.repository.impl;

import com.kinoicon.api.model.entity.PersonImageEntity;
import com.kinoicon.api.model.rowmapper.PersonImageRowMapper;
import com.kinoicon.api.repository.PersonImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PersonImageRepositoryImpl implements PersonImageRepository {

    private static final PersonImageRowMapper ROW_MAPPER = new PersonImageRowMapper();

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<PersonImageEntity> findByPersonId(long personId) {
        return jdbcTemplate.query("SELECT id, person_id, image_url, comment, tag FROM person_image WHERE person_id = ?", ROW_MAPPER, personId);
    }

    @Override
    @Transactional
    public void replaceForPerson(long personId, List<PersonImageEntity> images) {
        jdbcTemplate.update("DELETE FROM person_image WHERE person_id = ?", personId);
        if (images == null) {
            return;
        }
        for (PersonImageEntity image : images) {
            jdbcTemplate.update("INSERT INTO person_image (person_id, image_url, comment, tag) VALUES (?, ?, ?, ?)",
                    personId, image.getImageUrl(), image.getComment(), image.getTag());
        }
    }
}
