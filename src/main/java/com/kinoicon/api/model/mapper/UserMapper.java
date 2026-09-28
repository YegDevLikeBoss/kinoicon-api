package com.kinoicon.api.model.mapper;

import com.kinoicon.api.model.entity.UserEntity;
import com.kinoicon.api.model.request.CredentialsRequest;

import java.util.List;

public class UserMapper {

    private UserMapper() {}

    /** Password must already be hashed by the caller (BCrypt) - mapping stays a pure field walk. */
    public static UserEntity toNewEntity(CredentialsRequest request, String hashedPassword) {
        UserEntity entity = new UserEntity();
        entity.setUsername(request.username());
        entity.setPassword(hashedPassword);
        entity.setRights(List.of("USER"));
        return entity;
    }
}
