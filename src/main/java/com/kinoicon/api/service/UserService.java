package com.kinoicon.api.service;

import com.kinoicon.api.model.entity.UserEntity;
import com.kinoicon.api.model.request.CredentialsRequest;

public interface UserService {
    void register(CredentialsRequest request);
    String login(CredentialsRequest request);
    UserEntity findByUsername(String username);
}
