package com.kinoicon.api.repository;

import com.kinoicon.api.model.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByUsername(String username);
    UserEntity save(UserEntity user);
}
