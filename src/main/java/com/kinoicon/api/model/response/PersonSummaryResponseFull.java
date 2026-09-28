package com.kinoicon.api.model.response;

import com.kinoicon.api.model.response.value.NameResponse;

/** "summary" size for Person - PersonSummary schema. Not to be confused with
 *  model.response.value.PersonSummaryResponse, which is the compact reference used
 *  inside CrewMember (person field) and carries the exact same shape but a distinct
 *  identity/purpose in the API. */
public record PersonSummaryResponseFull(String id, Long shortId, NameResponse name, String coverUrl) {}
