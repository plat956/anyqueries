package by.latushko.anyqueries.model.mapper.impl;

import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.*;

public class UserMapper implements RowMapper<User> {
    @Override
    public Optional<User> mapRow(ResultSet resultSet, String fieldPrefix) {
        try {
            User user = new User();
            user.setId(resultSet.getLong(fieldPrefix + USER_ID));
            user.setFirstName(resultSet.getString(fieldPrefix + USER_FIRST_NAME));
            user.setLastName(resultSet.getString(fieldPrefix + USER_LAST_NAME));
            user.setMiddleName(resultSet.getString(fieldPrefix + USER_MIDDLE_NAME));
            user.setLogin(resultSet.getString(fieldPrefix + USER_LOGIN));
            user.setPassword(resultSet.getString(fieldPrefix + USER_PASSWORD));
            user.setEmail(resultSet.getString(fieldPrefix + USER_EMAIL));
            user.setTelegram(resultSet.getString(fieldPrefix + USER_TELEGRAM));
            user.setAvatar(resultSet.getString(fieldPrefix + USER_AVATAR));
            user.setLastLoginDate(resultSet.getObject(fieldPrefix + USER_LAST_LOGIN_DATE, LocalDateTime.class));
            user.setCredentialKey(resultSet.getString(fieldPrefix + USER_CREDENTIAL_KEY));
            user.setStatus(User.Status.valueOf(resultSet.getString(fieldPrefix + USER_STATUS)));
            user.setRole(User.Role.valueOf(resultSet.getString(fieldPrefix + USER_ROLE)));
            return Optional.of(user);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
