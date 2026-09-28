package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.PersonMediaEntity;
import com.kinoicon.api.model.request.PersonMediaRequest;
import com.kinoicon.api.model.response.PersonMediaResponse;

import java.util.List;

public class PersonMediaMapper {

    private PersonMediaMapper() {}

    public static PersonMediaResponse toResponse(PersonMediaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new PersonMediaResponse(
                entity.getCoverUrl(),
                entity.getImages() == null ? List.of() : entity.getImages().stream().map(ImageWithNoteMapper::toResponse).toList()
        );
    }

    public static PersonMediaEntity toEntity(PersonMediaRequest request, Long personId) {
        if (request == null) {
            return null;
        }
        return new PersonMediaEntity(
                request.cover_url(),
                request.images().stream().map(imageRequest -> ImageWithNoteMapper.toPersonImageEntity(imageRequest, personId)).toList()
        );
    }
}
