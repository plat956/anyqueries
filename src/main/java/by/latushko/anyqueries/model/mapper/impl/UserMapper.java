package by.latushko.anyqueries.model.mapper.impl;

import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserMapper implements RowMapper<User> {
    @Override
    public Optional<User> mapRow(ResultSet resultSet) {
        try {
            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
            user.setMiddleName(resultSet.getString("middle_name"));
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getString("password"));
            user.setEmail(resultSet.getString("email"));
            user.setAvatar(resultSet.getString("avatar"));
            user.setStatus(User.Status.valueOf(resultSet.getString("status")));
            user.setRole(User.Role.valueOf(resultSet.getString("role")));
            return Optional.of(user);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
