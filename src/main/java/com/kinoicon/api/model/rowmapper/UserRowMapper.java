package com.kinoicon.api.model.rowmapper;

import com.kinoicon.api.model.entity.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class UserRowMapper implements RowMapper<UserEntity> {

    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEntity entity = new UserEntity();
        entity.setId(rs.getLong("id"));
        entity.setUuid(UUID.fromString(rs.getString("uuid")));
        entity.setUsername(rs.getString("username"));
        entity.setPassword(rs.getString("password"));

        Array rightsArray = rs.getArray("rights");
        List<String> rights = rightsArray == null
                ? List.of()
                : Arrays.asList((String[]) rightsArray.getArray());
        entity.setRights(rights);

        return entity;
    }
}
