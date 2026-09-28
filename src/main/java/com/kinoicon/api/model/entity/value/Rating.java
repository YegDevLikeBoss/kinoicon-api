package com.kinoicon.api.model.entity.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    private RatingItem imdb;
    private RatingItem kp;
    private Integer metacritic;
    private Double rottentomatos;
}
