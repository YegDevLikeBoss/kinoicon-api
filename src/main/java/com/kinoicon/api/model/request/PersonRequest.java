package com.kinoicon.api.model.request;

import com.kinoicon.api.model.request.value.DateAndPlaceRequest;
import com.kinoicon.api.model.request.value.ImageWithNoteRequest;
import com.kinoicon.api.model.request.value.MetadataRequest;
import com.kinoicon.api.model.request.value.NameRequest;

import java.util.List;

/** PUT /person/{id} payload - partial update, same semantics as FilmRequest. */
public record PersonRequest(
        NameRequest name,
        DateAndPlaceRequest born,
        DateAndPlaceRequest died,
        Integer length,
        PersonMediaRequest media,
        MetadataRequest metadata
) {}
