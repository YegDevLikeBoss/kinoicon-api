package com.kinoicon.api.model.entity.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingItem {
    private String rating;
    private Integer voteCount;
}
