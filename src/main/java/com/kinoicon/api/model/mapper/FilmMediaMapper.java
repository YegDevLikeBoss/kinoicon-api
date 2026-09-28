package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.FilmMediaEntity;
import com.kinoicon.api.model.request.FilmMediaRequest;
import com.kinoicon.api.model.response.FilmMediaResponse;

import java.util.List;

public class FilmMediaMapper {

    private FilmMediaMapper() {}

    public static FilmMediaResponse toResponse(FilmMediaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new FilmMediaResponse(
                entity.getCoverUrl(),
                entity.getImages() == null ? List.of() : entity.getImages().stream().map(ImageWithNoteMapper::toResponse).toList(),
                entity.getTrailers() == null ? List.of() : entity.getTrailers().stream().map(TrailerMapper::toResponse).toList()
        );
    }

    public static FilmMediaEntity toEntity(FilmMediaRequest request, Long filmId) {
        if (request == null) {
            return null;
        }
        return new FilmMediaEntity(
                request.cover_url(),
                request.images().stream().map(imageRequest -> ImageWithNoteMapper.toFilmImageEntity(imageRequest, filmId)).toList(),
                request.trailers().stream().map(trailerRequest -> TrailerMapper.toEntity(trailerRequest, filmId)).toList()
        );
    }
}
