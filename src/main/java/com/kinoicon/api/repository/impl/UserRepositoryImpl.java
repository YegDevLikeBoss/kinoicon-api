package com.kinoicon.api.repository.impl;

import com.kinoicon.api.model.entity.UserEntity;
import com.kinoicon.api.model.rowmapper.UserRowMapper;
import com.kinoicon.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private static final UserRowMapper ROW_MAPPER = new UserRowMapper();

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        List<UserEntity> result = jdbcTemplate.query(
                "SELECT id, uuid, username, password, rights FROM app_user WHERE username = ?",
                ROW_MAPPER, username);
        return result.stream().findFirst();
    }

    @Override
    public UserEntity save(UserEntity user) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO app_user (username, password, rights) VALUES (?, ?, ?) ");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setArray(3, con.createArrayOf("text", user.getRights().toArray()));
            return ps;
        });
        return findByUsername(user.getUsername()).orElseThrow();
    }
}
