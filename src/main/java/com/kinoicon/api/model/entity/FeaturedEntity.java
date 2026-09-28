package com.kinoicon.api.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class FeaturedEntity {
    private Long id;
    private String title;
    private List<FeaturedRowEntity> rows;
}
