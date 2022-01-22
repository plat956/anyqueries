package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The User Data Access Object interface.
 * Describes signatures of this DAO implementation methods
 * Contains abstract methods to create extended CRUD operations for User entity
 */
public interface UserDao {
    /**
     * Create user hash.
     *
     * @param hash the new UserHash entity object
     * @return the boolean, true if the user hash was created successfully, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean createUserHash(UserHash hash) throws DaoException;

    /**
     * Find inactive by hash and hash is not expired.
     *
     * @param hash      the hash
     * @param validDate the valid date
     * @return the optional with found user or empty one
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    Optional<User> findInactiveByHashAndHashIsNotExpired(String hash, LocalDateTime validDate) throws DaoException;

    /**
     * Find inactive by telegram.
     *
     * @param account the telegram account
     * @return the optional with found user or empty one
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    Optional<User> findInactiveByTelegram(String account) throws DaoException;

    /**
     * Find by login.
     *
     * @param login the user login
     * @return the optional with found user or empty one
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    Optional<User> findByLogin(String login) throws DaoException;

    /**
     * Find by status and credential key.
     *
     * @param status the user status
     * @param key    the credential key
     * @return the optional with found user or empty one
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    Optional<User> findByStatusAndCredentialKey(User.Status status, String key) throws DaoException;

    /**
     * Find login by login contains order by login asc limited to.
     *
     * @param loginPattern the user login pattern
     * @param limit       the limit
     * @return the list of found user logins
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    List<String> findLoginByLoginContainsOrderByLoginAscLimitedTo(String loginPattern, int limit) throws DaoException;

    /**
     * Find by login contains order by role asc limited to.
     *
     * @param loginPattern the user login pattern
     * @param offset       the offset
     * @param limit        the limit
     * @return the list of found users
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    List<User> findByLoginContainsOrderByRoleAscLimitedTo(String loginPattern, int offset, int limit) throws DaoException;

    /**
     * Exists by login.
     *
     * @param login the user login
     * @return the boolean, true if user exists, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean existsByLogin(String login) throws DaoException;

    /**
     * Exists by email.
     *
     * @param email the user email
     * @return the boolean, true if user exists, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean existsByEmail(String email) throws DaoException;

    /**
     * Exists by telegram.
     *
     * @param telegram the telegram account
     * @return the boolean, true if user exists, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean existsByTelegram(String telegram) throws DaoException;

    /**
     * Exists by email except user id.
     *
     * @param email  the user email
     * @param userId the user id
     * @return the boolean, true if user exists, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean existsByEmailExceptUserId(String email, Long userId) throws DaoException;

    /**
     * Exists by telegram except user id.
     *
     * @param telegram the telegram account
     * @param userId   the user id
     * @return the boolean, true if user exists, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean existsByTelegramExceptUserId(String telegram, Long userId) throws DaoException;

    /**
     * Exists by login except user id.
     *
     * @param login  the user login
     * @param userId the user id
     * @return the boolean, true if user exists, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean existsByLoginExceptUserId(String login, Long userId) throws DaoException;

    /**
     * Delete user hash by user id.
     *
     * @param id the user id
     * @return the boolean, true if user was deleted successfully, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean deleteUserHashByUserId(Long id) throws DaoException;
}
