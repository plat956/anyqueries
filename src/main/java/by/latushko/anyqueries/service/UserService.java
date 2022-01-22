package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;

import java.util.List;
import java.util.Optional;

/**
 * The User service interface.
 */
public interface UserService {
    /**
     * Find by login and password.
     *
     * @param login    the login
     * @param password the password
     * @return the optional with the found user or empty one
     */
    Optional<User> findByLoginAndPassword(String login, String password);

    /**
     * Find active by credential key and credential token.
     *
     * @param key   the key
     * @param token the token
     * @return the optional with the found user or empty one
     */
    Optional<User> findActiveByCredentialKeyAndCredentialToken(String key, String token);

    /**
     * Find by id.
     *
     * @param id the user id
     * @return the optional with the found user or empty one
     */
    Optional<User> findById(Long id);

    /**
     * Find paginated by login contains order by role asc.
     *
     * @param page         the page
     * @param loginPattern the user login pattern
     * @return the paginated with the found user objects
     */
    Paginated<User> findPaginatedByLoginContainsOrderByRoleAsc(RequestPage page, String loginPattern);

    /**
     * Find login by login contains order by login asc limited to.
     *
     * @param loginPattern the user login pattern
     * @param limit        the limit
     * @return the list of found user logins
     */
    List<String> findLoginByLoginContainsOrderByLoginAscLimitedTo(String loginPattern, int limit);

    /**
     * Exists by login.
     *
     * @param login the user login
     * @return the boolean, true if the user exists, otherwise false
     */
    boolean existsByLogin(String login);

    /**
     * Exists by email.
     *
     * @param email the user email
     * @return the boolean, true if the user exists, otherwise false
     */
    boolean existsByEmail(String email);

    /**
     * Exists by telegram.
     *
     * @param telegram the user telegram account
     * @return the boolean, true if the user exists, otherwise false
     */
    boolean existsByTelegram(String telegram);

    /**
     * Exists by email except user id.
     *
     * @param email  the email
     * @param userId the user id
     * @return the boolean, true if the user exists, otherwise false
     */
    boolean existsByEmailExceptUserId(String email, Long userId);

    /**
     * Exists by telegram except user id.
     *
     * @param telegram the telegram
     * @param userId   the user id
     * @return the boolean, true if the user exists, otherwise false
     */
    boolean existsByTelegramExceptUserId(String telegram, Long userId);

    /**
     * Exists by login except user id.
     *
     * @param login  the user login
     * @param userId the user id
     * @return the boolean, true if the user exists, otherwise false
     */
    boolean existsByLoginExceptUserId(String login, Long userId);

    /**
     * Update last login date.
     *
     * @param user the user
     * @return the boolean, true if the user last login date was updated successfully, otherwise false
     */
    boolean updateLastLoginDate(User user);

    /**
     * Update user.
     *
     * @param user       the user
     * @param firstName  the first name
     * @param lastName   the last name
     * @param middleName the middle name
     * @param email      the email
     * @param telegram   the telegram
     * @param login      the login
     * @return the boolean, true if the user was updated successfully, otherwise false
     */
    boolean update(User user, String firstName, String lastName, String middleName, String email, String telegram, String login);

    /**
     * Update user.
     *
     * @param userId     the user id
     * @param firstName  the first name
     * @param lastName   the last name
     * @param middleName the middle name
     * @param email      the email
     * @param telegram   the telegram
     * @param login      the login
     * @param status     the status
     * @param role       the role
     * @return the boolean, true if the user was updated successfully, otherwise false
     */
    boolean update(Long userId, String firstName, String lastName, String middleName, String email, String telegram, String login,
                   User.Status status, User.Role role);

    /**
     * Update user avatar.
     *
     * @param user   the user
     * @param avatar the avatar
     * @return the boolean, true if the user avatar was updated successfully, otherwise false
     */
    boolean updateAvatar(User user, String avatar);

    /**
     * Delete user by id.
     *
     * @param id the user id
     * @return the boolean, true if the user was deleted successfully, otherwise false
     */
    boolean delete(Long id);

    /**
     * Create user object.
     *
     * @param firstName  the first name
     * @param lastName   the last name
     * @param middleName the middle name
     * @param email      the email
     * @param telegram   the telegram
     * @param login      the login
     * @param password   the password
     * @return the created user object
     */
    User createUserObject(String firstName, String lastName, String middleName, String email, String telegram, String login, String password);

    /**
     * Generate user activation hash.
     *
     * @param user the user
     * @return the generated user activation hash
     */
    UserHash generateUserHash(User user);

    /**
     * Generate credential token.
     *
     * @param user the user
     * @return the generated credential token
     */
    String generateCredentialToken(User user);

    /**
     * Change user password.
     *
     * @param user     the user
     * @param password the input password
     * @return the boolean, true if the user password was changed successfully, otherwise false
     */
    boolean changePassword(User user, String password);

    /**
     * Check user password.
     *
     * @param user     the user
     * @param password the input password
     * @return the boolean, true if the user password is correct, otherwise false
     */
    boolean checkPassword(User user, String password);
}
