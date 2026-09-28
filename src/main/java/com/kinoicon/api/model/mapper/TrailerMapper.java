package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.FilmTrailerEntity;
import com.kinoicon.api.model.request.value.TrailerRequest;
import com.kinoicon.api.model.response.value.TrailerResponse;

public class TrailerMapper {

    private TrailerMapper() {}

    public static FilmTrailerEntity toEntity(TrailerRequest request, long filmId) {
        FilmTrailerEntity entity = new FilmTrailerEntity();
        entity.setFilmId(filmId);
        entity.setLanguage(request.language());
        entity.setTitle(request.title());
        entity.setVideoUrl(request.video_url());
        return entity;
    }

    public static TrailerResponse toResponse(FilmTrailerEntity entity) {
        if (entity == null) {
            return null;
        }
        return new TrailerResponse(entity.getLanguage(), entity.getTitle(), entity.getVideoUrl());
    }
}
