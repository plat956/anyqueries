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
    public Optional<User> mapRow(ResultSet resultSet, String prefix) {
        try {
            User user = new User();
            user.setId(resultSet.getLong(prefix + USER_ID));
            user.setFirstName(resultSet.getString(prefix + USER_FIRST_NAME));
            user.setLastName(resultSet.getString(prefix + USER_LAST_NAME));
            user.setMiddleName(resultSet.getString(prefix + USER_MIDDLE_NAME));
            user.setLogin(resultSet.getString(prefix + USER_LOGIN));
            user.setPassword(resultSet.getString(prefix + USER_PASSWORD));
            user.setEmail(resultSet.getString(prefix + USER_EMAIL));
            user.setTelegram(resultSet.getString(prefix + USER_TELEGRAM));
            user.setAvatar(resultSet.getString(prefix + USER_AVATAR));
            user.setLastLoginDate(resultSet.getObject(prefix + USER_LAST_LOGIN_DATE, LocalDateTime.class));
            user.setCredentialKey(resultSet.getString(prefix + USER_CREDENTIAL_KEY));
            user.setStatus(User.Status.valueOf(resultSet.getString(prefix + USER_STATUS)));
            user.setRole(User.Role.valueOf(resultSet.getString(prefix + USER_ROLE)));
            if(hasColumn(resultSet, prefix + TOTAL)) {
                user.setTotal(resultSet.getLong(prefix + TOTAL));
            }
            return Optional.of(user);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
