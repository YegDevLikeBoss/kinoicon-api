package com.kinoicon.api.model.entity;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserEntity {
    private Long id;
    private UUID uuid;
    private String username;
    private String password;
    private List<String> rights;
}
