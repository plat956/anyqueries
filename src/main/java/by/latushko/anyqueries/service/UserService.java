package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;

import java.util.Optional;

public interface UserService {
    String AVATAR_PREFIX = "avatar_";
    int AVATAR_MAX_SIZE = 190;

    User createNewUser(String firstName, String lastName, String middleName, String email,
                       String telegram, String login, String password);
    UserHash generateUserHash(User user);
    Optional<User> findUserByLogin(String login);
    Optional<User> findUserByCredentialKey(String key);
    boolean updateLastLoginDate(User user);
    Optional<User> findIfExistsByLoginAndPassword(String login, String password);
    Optional<User> findIfExistsByCredentialsKeyAndToken(String key, String token);
    String getCredentialToken(User user);
    boolean checkIfExistsByLogin(String login);
    boolean checkIfExistsByEmail(String email);
    boolean checkIfExistsByTelegram(String telegram);
    boolean checkIfExistsByEmailExceptUserId(String email, Long userId);
    boolean checkIfExistsByTelegramExceptUserId(String telegram, Long userId);
    boolean checkIfExistsByLoginExceptUserId(String login, Long userId);
    boolean updateUserData(User user, String firstName, String lastName, String middleName, String email, String telegram, String login);
    boolean changePassword(User user, String password);
    boolean checkPassword(User user, String password);
    boolean updateAvatar(User user, String avatar);
    boolean resizeAvatar(String avatar);
    String findUserAvatar(Long userId);
}
