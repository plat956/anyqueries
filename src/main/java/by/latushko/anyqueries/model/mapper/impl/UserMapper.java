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
    public Optional<User> mapRow(ResultSet resultSet) {
        try {
            User user = new User();
            user.setId(resultSet.getLong(USER_ID));
            user.setFirstName(resultSet.getString(USER_FIRST_NAME));
            user.setLastName(resultSet.getString(USER_LAST_NAME));
            user.setMiddleName(resultSet.getString(USER_MIDDLE_NAME));
            user.setLogin(resultSet.getString(USER_LOGIN));
            user.setPassword(resultSet.getString(USER_PASSWORD));
            user.setEmail(resultSet.getString(USER_EMAIL));
            user.setTelegram(resultSet.getString(USER_TELEGRAM));
            user.setAvatar(resultSet.getString(USER_AVATAR));
            user.setLastLoginDate(resultSet.getObject(USER_LAST_LOGIN_DATE, LocalDateTime.class));
            user.setCredentialKey(resultSet.getString(USER_CREDENTIAL_KEY));
            user.setStatus(User.Status.valueOf(resultSet.getString(USER_STATUS)));
            user.setRole(User.Role.valueOf(resultSet.getString(USER_ROLE)));
            return Optional.of(user);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
