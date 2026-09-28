package com.kinoicon.api.service.impl;

import com.kinoicon.api.exception.BaseException;
import com.kinoicon.api.model.entity.FilmCrewEntity;
import com.kinoicon.api.model.entity.FilmEntity;
import com.kinoicon.api.model.entity.PersonEntity;
import com.kinoicon.api.model.enums.SizeType;
import com.kinoicon.api.model.mapper.FilmMapper;
import com.kinoicon.api.model.mapper.ImageWithNoteMapper;
import com.kinoicon.api.model.mapper.PersonMapper;
import com.kinoicon.api.model.request.PersonRequest;
import com.kinoicon.api.model.response.value.PersonPictureResponse;
import com.kinoicon.api.repository.FilmCrewRepository;
import com.kinoicon.api.repository.FilmRepository;
import com.kinoicon.api.repository.PersonImageRepository;
import com.kinoicon.api.repository.PersonRepository;
import com.kinoicon.api.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonImageRepository personImageRepository;
    private final FilmCrewRepository filmCrewRepository;
    private final FilmRepository filmRepository;

    @Override
    public Object getById(long id, SizeType size, boolean authenticated) {
        PersonEntity person = personRepository.findById(id, authenticated).orElseThrow(BaseException::personNotFound);

        return switch (size) {
            case FULL -> {
                person.getMedia().setImages(personImageRepository.findByPersonId(id));
                List<PersonPictureResponse> pictures = buildPictureResponses(id, authenticated);
                yield PersonMapper.toResponse(person, pictures);
            }
            case SUMMARY -> PersonMapper.toSummaryResponse(person);
            case MINIMAL -> PersonMapper.toMinimalResponse(person);
        };
    }

    /** Filters out pictures whose linked film is unpublished when the viewer is anonymous -
     *  the Person-side mirror of Film's crew filtering, same filter_unpublished(...) rule. */
    private List<PersonPictureResponse> buildPictureResponses(long personId, boolean authenticated) {
        List<FilmCrewEntity> crewRows = filmCrewRepository.findByPersonId(personId);
        List<Long> filmIds = crewRows.stream().map(FilmCrewEntity::getFilmId).distinct().toList();
        Map<Long, FilmEntity> films = filmRepository.findByIds(filmIds).stream()
                .collect(Collectors.toMap(FilmEntity::getId, f -> f));

        return crewRows.stream()
                .map(c -> Map.entry(c, films.get(c.getFilmId())))
                .filter(e -> e.getValue() != null && (authenticated || FilmMapper.isPublished(e.getValue())))
                .map(e -> PersonMapper.toPictureResponse(e.getKey(), e.getValue()))
                .toList();
    }

    @Override
    public long create() {
        return personRepository.create();
    }

    @Override
    public void update(long id, PersonRequest request) {
        PersonEntity entity = personRepository.findById(id, true).orElseThrow(BaseException::personNotFound);
        PersonMapper.applyPartialUpdate(entity, request);
        personRepository.update(entity);

        if (request.media() != null && request.media().images() != null) {
            personImageRepository.replaceForPerson(id, request.media().images().stream()
                    .map(img -> ImageWithNoteMapper.toPersonImageEntity(img, id)).toList());
        }
    }

    @Override
    public void delete(long id) {
        if (!personRepository.deleteById(id)) {
            throw BaseException.personNotFound();
        }
    }
}
