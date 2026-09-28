package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.FilmCrewEntity;
import com.kinoicon.api.model.entity.FilmEntity;
import com.kinoicon.api.model.entity.PersonEntity;
import com.kinoicon.api.model.request.FilmRequest;
import com.kinoicon.api.model.request.value.CrewMemberRequest;
import com.kinoicon.api.model.response.FilmResponse;
import com.kinoicon.api.model.response.FilmSummaryResponse;
import com.kinoicon.api.model.response.value.CrewMemberResponse;
import com.kinoicon.api.model.response.value.FilmMinimalResponse;
import com.kinoicon.api.model.response.value.PersonSummaryResponse;

import java.util.List;
import java.util.Map;

/**
 * Entity <-> DTO mapping for Film, field by field, by hand.
 * Cross-entity data (crew -> person summaries) is assembled by the service layer and
 * handed in as already-built response lists/strings, since a pure Film mapper has no
 * business looking up Person rows itself.
 */
public class FilmMapper {

    private FilmMapper() {}

    public static FilmResponse toResponse(FilmEntity entity, List<CrewMemberResponse> crew) {
        if (entity == null) {
            return null;
        }
        return new FilmResponse(
                entity.getUuid().toString(),
                entity.getId(),
                NameMapper.toResponse(entity.getName()),
                entity.getDate(),
                entity.getDuration(),
                LoglineMapper.toResponse(entity.getLogline()),
                PriceMapper.toResponse(entity.getBudget()),
                GrossMapper.toResponse(entity.getGross()),
                ExternalMapper.toResponse(entity.getExternal()),
                RatingMapper.toResponse(entity.getRating()),
                FilmMediaMapper.toResponse(entity.getMedia()),
                crew,
                MetadataMapper.toResponse(entity.getMetadata())
        );
    }

    public static FilmSummaryResponse toSummaryResponse(FilmEntity entity, String director, String mainActors) {
        if (entity == null) {
            return null;
        }
        return new FilmSummaryResponse(
                entity.getUuid().toString(),
                entity.getId(),
                NameMapper.toResponse(entity.getName()),
                entity.getDate(),
                (entity.getMedia() != null) ? entity.getMedia().getCoverUrl() : null,
                director,
                mainActors
        );
    }

    public static FilmMinimalResponse toMinimalResponse(FilmEntity entity) {
        if (entity == null) {
            return null;
        }
        return new FilmMinimalResponse(
                entity.getUuid().toString(),
                entity.getId(),
                NameMapper.toResponse(entity.getName()),
                entity.getDate(),
                (entity.getMedia() != null) ? entity.getMedia().getCoverUrl() : null
        );
    }

    /** Builds one crew entry combining the junction row with the referenced person's summary fields. */
    public static CrewMemberResponse toCrewMemberResponse(FilmCrewEntity crew, PersonEntity person) {
        PersonSummaryResponse personSummary = new PersonSummaryResponse(
                person.getUuid().toString(),
                person.getId(),
                NameMapper.toResponse(person.getName()),
                (person.getMedia() != null) ? person.getMedia().getCoverUrl() : null
        );
        return new CrewMemberResponse(
                personSummary,
                crew.isLead(),
                crew.getLeadingRoles(),
                crew.getOtherRoles(),
                crew.getNote()
        );
    }

    /** Applies a partial FilmRequest onto an existing entity - only present (non-null) fields overwrite. */
    public static void applyPartialUpdate(FilmEntity entity, FilmRequest request) {
        if (request.name() != null) {
            entity.setName(NameMapper.toEntity(request.name()));
        }
        if (request.date() != null) {
            entity.setDate(request.date());
        }
        if (request.duration() != null) {
            entity.setDuration(request.duration());
        }
        if (request.logline() != null) {
            entity.setLogline(LoglineMapper.toEntity(request.logline()));
        }
        if (request.budget() != null) {
            entity.setBudget(PriceMapper.toEntity(request.budget()));
        }
        if (request.gross() != null) {
            entity.setGross(GrossMapper.toEntity(request.gross()));
        }
        if (request.external() != null) {
            entity.setExternal(ExternalMapper.toEntity(request.external()));
        }
        if (request.media() != null) {
            entity.setMedia(FilmMediaMapper.toEntity(request.media(), entity.getId()));
        }
        if (request.metadata() != null && request.metadata().published() != null) {
            entity.getMetadata().setPublished(request.metadata().published());
        }
    }

    public static FilmCrewEntity toCrewEntity(CrewMemberRequest request, Map<String, Long> personUuidIdMap) {
        FilmCrewEntity entity = new FilmCrewEntity();
        entity.setPersonId(personUuidIdMap.get(request.person())); // TODO now members is checked with uuid but will change to id in future
        entity.setLead(request.is_lead());
        entity.setLeadingRoles(request.leading_roles());
        entity.setOtherRoles(request.other_roles());
        entity.setNote(request.note());
        return entity;
    }

    public static boolean isPublished(FilmEntity entity) {
        return entity.getMetadata() != null && entity.getMetadata().isPublished();
    }
}
