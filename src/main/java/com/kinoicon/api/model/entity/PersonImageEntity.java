package com.kinoicon.api.model.entity;

@lombok.Data
public class PersonImageEntity {
    private Long id;
    private Long personId;
    private String imageUrl;
    private String comment;
    private String tag;
}
