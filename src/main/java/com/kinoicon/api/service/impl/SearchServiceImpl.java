package com.kinoicon.api.service.impl;

import com.kinoicon.api.config.SearchProperties;
import com.kinoicon.api.exception.BaseException;
import com.kinoicon.api.model.mapper.FilmMapper;
import com.kinoicon.api.model.mapper.PersonMapper;
import com.kinoicon.api.model.response.SearchResponse;
import com.kinoicon.api.model.response.SearchResultResponse;
import com.kinoicon.api.model.response.value.FilmMinimalResponse;
import com.kinoicon.api.repository.FilmRepository;
import com.kinoicon.api.repository.PersonRepository;
import com.kinoicon.api.repository.ScoredFilm;
import com.kinoicon.api.repository.ScoredPerson;
import com.kinoicon.api.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Ports search.py's "best" selection logic 1:1: whichever of the top film/person result has
 * the (strictly) higher similarity score becomes "best" and is removed from its list; ties go
 * to the film, matching the original `if film_score < person_score` (strict less-than).
 */
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final FilmRepository filmRepository;
    private final PersonRepository personRepository;
    private final SearchProperties searchProperties;

    @Override
    public SearchResponse search(String query, boolean authenticated) {
        if (query == null || query.isEmpty()) {
            throw BaseException.emptySearchQuery();
        }

        List<ScoredFilm> films = new ArrayList<>(filmRepository.search(
                query, authenticated, searchProperties.getSimilarityThreshold(), searchProperties.getLimit()));
        List<ScoredPerson> persons = new ArrayList<>(personRepository.search(
                query, authenticated, searchProperties.getSimilarityThreshold(), searchProperties.getLimit()));

        SearchResultResponse best = null;
        if (!films.isEmpty() && !persons.isEmpty()) {
            double filmScore = films.get(0).score();
            double personScore = persons.get(0).score();
            if (filmScore < personScore) {
                best = new SearchResultResponse("person", PersonMapper.toMinimalResponse(persons.remove(0).entity()));
            } else {
                best = new SearchResultResponse("film", FilmMapper.toMinimalResponse(films.remove(0).entity()));
            }
        } else if (!films.isEmpty()) {
            best = new SearchResultResponse("film", FilmMapper.toMinimalResponse(films.remove(0).entity()));
        } else if (!persons.isEmpty()) {
            best = new SearchResultResponse("person", PersonMapper.toMinimalResponse(persons.remove(0).entity()));
        }

        List<FilmMinimalResponse> filmResponses = films.stream().map(f -> FilmMapper.toMinimalResponse(f.entity())).toList();
        List<com.kinoicon.api.model.response.PersonMinimalResponse> personResponses =
                persons.stream().map(p -> PersonMapper.toMinimalResponse(p.entity())).toList();

        return new SearchResponse(
                filmResponses.isEmpty() ? null : filmResponses,
                personResponses.isEmpty() ? null : personResponses,
                best
        );
    }
}
