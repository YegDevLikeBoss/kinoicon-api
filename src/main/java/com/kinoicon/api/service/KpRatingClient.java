package com.kinoicon.api.service;

import java.util.Optional;

public interface KpRatingClient {

    record KpRating(String kpRating, int kpVoteCount, String imdbRating, int imdbVoteCount) {}

    /** Best-effort fetch: returns empty on any non-200 response or parse failure, never throws. */
    Optional<KpRating> fetchRating(String kpId);
}
