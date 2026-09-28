package com.kinoicon.api.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class FeaturedRowEntity {
    private Long id;
    private Long featuredId;
    private int position;
    private String kind;
    private String title;
    private List<FeaturedRowItemEntity> items;
}
