package com.kinoicon.api.model.entity.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gross {
    private Price russia;
    private Price usa;
    private Price world;
}
