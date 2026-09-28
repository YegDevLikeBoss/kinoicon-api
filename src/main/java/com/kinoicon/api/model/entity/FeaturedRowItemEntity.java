package com.kinoicon.api.model.entity;

@lombok.Data
public class FeaturedRowItemEntity {
    private Long id;
    private Long rowId;
    private int position;
    private String itemKind;   // "film" | "person"
    private Long itemShortId;
}
