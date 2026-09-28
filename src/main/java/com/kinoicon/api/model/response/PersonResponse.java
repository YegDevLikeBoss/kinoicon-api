package com.kinoicon.api.model.response;

import com.kinoicon.api.model.response.value.DateAndPlaceResponse;
import com.kinoicon.api.model.response.value.ImageWithNoteResponse;
import com.kinoicon.api.model.response.value.MetadataResponse;
import com.kinoicon.api.model.response.value.NameResponse;
import com.kinoicon.api.model.response.value.PersonPictureResponse;

import java.util.List;

/** "full" size - mirrors the Person schema in the OpenAPI spec. */
public record PersonResponse(
        String id,
        Long shortId,
        NameResponse name,
        DateAndPlaceResponse born,
        DateAndPlaceResponse died,
        Integer length,
        PersonMediaResponse media,
        List<PersonPictureResponse> pictures,
        MetadataResponse metadata
) {}
