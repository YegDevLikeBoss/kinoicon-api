package com.kinoicon.api.model.request.value;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CrewMemberRequest(
        @NotNull String person, // TODO now members is checked with uuid but will change to id in future
        boolean is_lead,
        List<String> leading_roles,
        List<String> other_roles,
        String note
) {}
