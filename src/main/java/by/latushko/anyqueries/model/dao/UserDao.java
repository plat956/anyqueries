package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserDao {
    boolean createUserHash(UserHash hash) throws DaoException;
    boolean deleteUserHashByUserId(Long id) throws DaoException;
    Optional<User> findInactiveUserByHashAndHashIsNotExpired(String hash, LocalDateTime validDate) throws DaoException;
    Optional<User> findInactiveUserByTelegram(String account) throws DaoException;
    Optional<User> findUserByLogin(String login) throws DaoException;
    Optional<User> findUserByCredentialKey(String key) throws DaoException;
    boolean existsByLogin(String login) throws DaoException;
    boolean existsByEmail(String email) throws DaoException;
    boolean existsByTelegram(String telegram) throws DaoException;
}
