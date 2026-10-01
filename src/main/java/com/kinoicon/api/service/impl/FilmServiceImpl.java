package com.kinoicon.api.service.impl;

import com.kinoicon.api.exception.BaseException;
import com.kinoicon.api.model.entity.FilmCrewEntity;
import com.kinoicon.api.model.entity.FilmEntity;
import com.kinoicon.api.model.entity.PersonEntity;
import com.kinoicon.api.model.enums.SizeType;
import com.kinoicon.api.model.mapper.FilmMapper;
import com.kinoicon.api.model.mapper.ImageWithNoteMapper;
import com.kinoicon.api.model.mapper.TrailerMapper;
import com.kinoicon.api.model.request.FilmRequest;
import com.kinoicon.api.model.request.value.CrewMemberRequest;
import com.kinoicon.api.model.response.FilmResponse;
import com.kinoicon.api.model.response.FilmSummaryResponse;
import com.kinoicon.api.model.response.value.CrewMemberResponse;
import com.kinoicon.api.model.response.value.FilmMinimalResponse;
import com.kinoicon.api.repository.FilmCrewRepository;
import com.kinoicon.api.repository.FilmImageRepository;
import com.kinoicon.api.repository.FilmRepository;
import com.kinoicon.api.repository.FilmTrailerRepository;
import com.kinoicon.api.repository.PersonRepository;
import com.kinoicon.api.service.FilmService;
import com.kinoicon.api.service.KpRatingClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final FilmImageRepository filmImageRepository;
    private final FilmTrailerRepository filmTrailerRepository;
    private final FilmCrewRepository filmCrewRepository;
    private final PersonRepository personRepository;
    private final KpRatingClient kpRatingClient;

    @Override
    public Object getById(long id, SizeType size, boolean authenticated) {
        FilmEntity film = filmRepository.findById(id, authenticated).orElseThrow(BaseException::filmNotFound);

        List<FilmCrewEntity> crewRows = filmCrewRepository.findByFilmId(id);
        List<CrewMemberResponse> crew = buildCrewResponses(crewRows, authenticated);

        return switch (size) {
            case FULL -> {
                film.getMedia().setImages(filmImageRepository.findByFilmId(id));
                film.getMedia().setTrailers(filmTrailerRepository.findByFilmId(id));
                yield FilmMapper.toResponse(film, crew);
            }
            case SUMMARY -> {
                List<PersonEntity> directors = filterByRole(crewRows, personsById(crewRows), "director");
                List<PersonEntity> mainActors = filterByRole(crewRows, personsById(crewRows), "actor");
                yield FilmMapper.toSummaryResponse(film, (!directors.isEmpty()) ? directors.getFirst() : new PersonEntity(), mainActors);
            }
            case MINIMAL -> FilmMapper.toMinimalResponse(film);
        };
    }

    /** Filters out crew whose linked person is unpublished when the viewer is anonymous,
     *  matching the original filter_unpublished(not bool(current_user)) behaviour. */
    private List<CrewMemberResponse> buildCrewResponses(List<FilmCrewEntity> crewRows, boolean authenticated) {
        Map<Long, PersonEntity> persons = personsById(crewRows);
        return crewRows.stream()
                .map(c -> Map.entry(c, persons.get(c.getPersonId())))
                .filter(e -> e.getValue() != null && (authenticated || com.kinoicon.api.model.mapper.PersonMapper.isPublished(e.getValue())))
                .map(e -> FilmMapper.toCrewMemberResponse(e.getKey(), e.getValue()))
                .toList();
    }

    private Map<Long, PersonEntity> personsById(List<FilmCrewEntity> crewRows) {
        List<Long> personIds = crewRows.stream().map(FilmCrewEntity::getPersonId).distinct().toList();
        return personRepository.findByIds(personIds).stream()
                .collect(Collectors.toMap(PersonEntity::getId, p -> p));
    }

    private List<PersonEntity> filterByRole(List<FilmCrewEntity> crewRows, Map<Long, PersonEntity> persons, String role) {
        return crewRows.stream()
                .filter(c -> hasRole(c, role))
                .map(c -> persons.get(c.getPersonId()))
                .filter(java.util.Objects::nonNull)
                .limit((role.equals("director")) ? 1 : 3)
                .toList();
    }

    // TODO obsolete
    private String joinNamesByRole(List<FilmCrewEntity> crewRows, Map<Long, PersonEntity> persons, String role) {
        return crewRows.stream()
                .filter(c -> hasRole(c, role))
                .map(c -> persons.get(c.getPersonId()))
                .filter(java.util.Objects::nonNull)
                .map(p -> p.getName() != null ? p.getName().getEn() : null)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.joining(", "));
    }

    private boolean hasRole(FilmCrewEntity crew, String role) {
        boolean inLeading = crew.getLeadingRoles() != null && crew.getLeadingRoles().stream().anyMatch(r -> r.equalsIgnoreCase(role));
        boolean inOther = crew.getOtherRoles() != null && crew.getOtherRoles().stream().anyMatch(r -> r.equalsIgnoreCase(role));
        return inLeading || inOther;
    }

    @Override
    public long create() {
        return filmRepository.create();
    }

    @Override
    public void update(long id, FilmRequest request) {
        FilmEntity entity = filmRepository.findById(id, true).orElseThrow(BaseException::filmNotFound);
        FilmMapper.applyPartialUpdate(entity, request);
        filmRepository.update(entity);

        if (request.media() != null && request.media().images() != null) {
            filmImageRepository.replaceForFilm(id, request.media().images().stream()
                    .map(img -> ImageWithNoteMapper.toFilmImageEntity(img, id)).toList());
        }
        if (request.media() != null && request.media().trailers() != null) {
            filmTrailerRepository.replaceForFilm(id, request.media().trailers().stream()
                    .map(t -> TrailerMapper.toEntity(t, id)).toList());
        }
        if (request.crew() != null) {
            Map<String, Long> personUuidIdMap = personRepository.findByUuids(request.crew().stream().map(CrewMemberRequest::person).toList())
                    .stream()
                    .collect(Collectors.toMap(
                            i -> i.getUuid().toString(),
                            PersonEntity::getId
                    ));
            List<FilmCrewEntity> crew = request.crew().stream().map(crewRequest -> FilmMapper.toCrewEntity(crewRequest, personUuidIdMap)).toList();
            filmCrewRepository.replaceForFilm(id, crew);
        }

        syncKpRatingIfNeeded(id, entity);
    }

    /** Mirrors the original PUT /film/{id} behaviour: after a successful update, if the film
     *  has a KP id, fetch fresh KP/IMDB rating and persist it. Silently does nothing if the
     *  external call fails - never fails the update request because of it. */
    private void syncKpRatingIfNeeded(long id, FilmEntity entity) {
        if (entity.getExternal() == null || entity.getExternal().getKpId() == null) {
            return;
        }
        kpRatingClient.fetchRating(entity.getExternal().getKpId()).ifPresent(rating ->
                filmRepository.updateRating(id, rating.kpRating(), rating.kpVoteCount(), rating.imdbRating(), rating.imdbVoteCount()));
    }

    @Override
    public void delete(long id) {
        if (!filmRepository.deleteById(id)) {
            throw BaseException.filmNotFound();
        }
    }
}
