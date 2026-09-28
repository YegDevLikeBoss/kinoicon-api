package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.FilmImageEntity;
import com.kinoicon.api.model.entity.PersonImageEntity;
import com.kinoicon.api.model.request.value.ImageWithNoteRequest;
import com.kinoicon.api.model.response.value.ImageWithNoteResponse;

public class ImageWithNoteMapper {

    private ImageWithNoteMapper() {}

    public static FilmImageEntity toFilmImageEntity(ImageWithNoteRequest request, long filmId) {
        FilmImageEntity entity = new FilmImageEntity();
        entity.setFilmId(filmId);
        entity.setImageUrl(request.image_url());
        entity.setComment(request.comment());
        entity.setTag(request.tag());
        return entity;
    }

    public static PersonImageEntity toPersonImageEntity(ImageWithNoteRequest request, long personId) {
        PersonImageEntity entity = new PersonImageEntity();
        entity.setPersonId(personId);
        entity.setImageUrl(request.image_url());
        entity.setComment(request.comment());
        entity.setTag(request.tag());
        return entity;
    }

    public static ImageWithNoteResponse toResponse(FilmImageEntity entity) {
        if (entity == null) {
            return null;
        }
        return new ImageWithNoteResponse(entity.getImageUrl(), entity.getComment(), entity.getTag());
    }

    public static ImageWithNoteResponse toResponse(PersonImageEntity entity) {
        if (entity == null) {
            return null;
        }
        return new ImageWithNoteResponse(entity.getImageUrl(), entity.getComment(), entity.getTag());
    }
}
