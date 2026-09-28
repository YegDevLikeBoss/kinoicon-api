package com.kinoicon.api.service.impl;

import com.kinoicon.api.config.JwtService;
import com.kinoicon.api.exception.BaseException;
import com.kinoicon.api.model.entity.UserEntity;
import com.kinoicon.api.model.mapper.UserMapper;
import com.kinoicon.api.model.request.CredentialsRequest;
import com.kinoicon.api.repository.UserRepository;
import com.kinoicon.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // cost factor 12, matching the original flask_bcrypt hashes ($2b$12$...)
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(12);

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void register(CredentialsRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(existing -> {
            throw BaseException.userAlreadyRegistered();
        });
        String hashed = PASSWORD_ENCODER.encode(request.password());
        UserEntity entity = UserMapper.toNewEntity(request, hashed);
        userRepository.save(entity);
    }

    @Override
    public String login(CredentialsRequest request) {
        UserEntity user = userRepository.findByUsername(request.username())
                .orElseThrow(BaseException::wrongUserCredentials);
        if (!PASSWORD_ENCODER.matches(request.password(), user.getPassword())) {
            throw BaseException.wrongUserCredentials();
        }
        return jwtService.generateToken(user.getUsername());
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
