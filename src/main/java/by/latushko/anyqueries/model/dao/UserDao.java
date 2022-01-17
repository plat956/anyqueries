package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    boolean createUserHash(UserHash hash) throws DaoException;
    Optional<User> findInactiveByHashAndHashIsNotExpired(String hash, LocalDateTime validDate) throws DaoException;
    Optional<User> findInactiveByTelegram(String account) throws DaoException;
    Optional<User> findByLogin(String login) throws DaoException;
    Optional<User> findByCredentialKey(String key) throws DaoException;
    List<String> findLoginByLoginContainsOrderByLoginAscLimitedTo(String loginPatter, int limit) throws DaoException;
    List<User> findByLoginContainsOrderByRoleAscLimitedTo(String loginPattern, int offset, int limit) throws DaoException;
    boolean existsByLogin(String login) throws DaoException;
    boolean existsByEmail(String email) throws DaoException;
    boolean existsByTelegram(String telegram) throws DaoException;
    boolean existsByEmailExceptUserId(String email, Long userId) throws DaoException;
    boolean existsByTelegramExceptUserId(String telegram, Long userId) throws DaoException;
    boolean existsByLoginExceptUserId(String login, Long userId) throws DaoException;
    boolean deleteUserHashByUserId(Long id) throws DaoException;
}
