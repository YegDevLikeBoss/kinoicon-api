package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.FilmCrewEntity;
import com.kinoicon.api.model.entity.FilmEntity;
import com.kinoicon.api.model.entity.PersonEntity;
import com.kinoicon.api.model.request.PersonRequest;
import com.kinoicon.api.model.response.PersonMinimalResponse;
import com.kinoicon.api.model.response.PersonResponse;
import com.kinoicon.api.model.response.PersonSummaryResponseFull;
import com.kinoicon.api.model.response.value.PersonPictureResponse;

import java.util.ArrayList;
import java.util.List;

public class PersonMapper {

    private PersonMapper() {}

    public static PersonResponse toResponse(PersonEntity entity, List<PersonPictureResponse> pictures) {
        if (entity == null) {
            return null;
        }
        return new PersonResponse(
                entity.getUuid().toString(),
                entity.getId(),
                NameMapper.toResponse(entity.getName()),
                DateAndPlaceMapper.toResponse(entity.getBorn()),
                DateAndPlaceMapper.toResponse(entity.getDied()),
                entity.getLength(),
                PersonMediaMapper.toResponse(entity.getMedia()),
                pictures,
                MetadataMapper.toResponse(entity.getMetadata())
        );
    }

    public static PersonSummaryResponseFull toSummaryResponse(PersonEntity entity) {
        if (entity == null) {
            return null;
        }
        return new PersonSummaryResponseFull(
                entity.getUuid().toString(),
                entity.getId(),
                NameMapper.toResponse(entity.getName()),
                (entity.getMedia() != null) ? entity.getMedia().getCoverUrl() : null
        );
    }

    public static PersonMinimalResponse toMinimalResponse(PersonEntity entity) {
        if (entity == null) {
            return null;
        }
        return new PersonMinimalResponse(
                entity.getUuid().toString(),
                entity.getId(),
                NameMapper.toResponse(entity.getName()),
                (entity.getMedia() != null) ? entity.getMedia().getCoverUrl() : null
        );
    }

    /** Builds one "picture" entry (this person's participation in one film) for Person.pictures. */
    public static PersonPictureResponse toPictureResponse(FilmCrewEntity crew, FilmEntity film) {
        List<String> roles = new ArrayList<>();
        if (crew.getLeadingRoles() != null) {
            roles.addAll(crew.getLeadingRoles());
        }
        if (crew.getOtherRoles() != null) {
            roles.addAll(crew.getOtherRoles());
        }
        return new PersonPictureResponse(FilmMapper.toMinimalResponse(film), roles, crew.getNote());
    }

    public static void applyPartialUpdate(PersonEntity entity, PersonRequest request) {
        if (request.name() != null) {
            entity.setName(NameMapper.toEntity(request.name()));
        }
        if (request.born() != null) {
            entity.setBorn(DateAndPlaceMapper.toEntity(request.born()));
        }
        if (request.died() != null) {
            entity.setDied(DateAndPlaceMapper.toEntity(request.died()));
        }
        if (request.length() != null) {
            entity.setLength(request.length());
        }
        if (request.media() != null) {
            entity.setMedia(PersonMediaMapper.toEntity(request.media(), entity.getId()));
        }
        if (request.metadata() != null && request.metadata().published() != null) {
            entity.getMetadata().setPublished(request.metadata().published());
        }
    }

    public static boolean isPublished(PersonEntity entity) {
        return entity.getMetadata() != null && entity.getMetadata().isPublished();
    }
}
