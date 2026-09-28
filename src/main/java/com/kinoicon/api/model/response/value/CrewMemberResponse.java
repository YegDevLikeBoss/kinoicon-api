package com.kinoicon.api.model.response.value;

import java.util.List;

public record CrewMemberResponse(
        PersonSummaryResponse person,
        boolean is_lead,
        List<String> leading_roles,
        List<String> other_roles,
        String note
) {}
