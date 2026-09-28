package com.kinoicon.api.model.request;

import com.kinoicon.api.model.request.value.CrewMemberRequest;
import com.kinoicon.api.model.request.value.ExternalRequest;
import com.kinoicon.api.model.request.value.GrossRequest;
import com.kinoicon.api.model.request.value.LoglineRequest;
import com.kinoicon.api.model.request.value.MetadataRequest;
import com.kinoicon.api.model.request.value.NameRequest;
import com.kinoicon.api.model.request.value.PriceRequest;

import java.time.LocalDate;
import java.util.List;

/** PUT /film/{id} payload. Every field is optional - the underlying update is partial,
 *  mirroring the Flask endpoint's `schema.load(payload, partial=True)` behaviour: only
 *  fields present in the JSON body get updated, the rest are left untouched. */
public record FilmRequest(
        NameRequest name,
        LocalDate date,
        Integer duration,
        LoglineRequest logline,
        PriceRequest budget,
        GrossRequest gross,
        ExternalRequest external,
        FilmMediaRequest media,
        List<CrewMemberRequest> crew,
        MetadataRequest metadata
) {}
