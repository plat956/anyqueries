package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;

import java.util.Optional;

public interface UserService {
    User createNewUser(String firstName, String lastName, String middleName, String email,
                       String telegram, String login, String password);
    Optional<User> findByLogin(String login);
    Optional<User> findByCredentialKey(String key);
    Optional<User> findByLoginAndPassword(String login, String password);
    Optional<User> findByCredentialsKeyAndToken(String key, String token);
    boolean checkIfExistsByLogin(String login);
    boolean checkIfExistsByEmail(String email);
    boolean checkIfExistsByTelegram(String telegram);
    boolean checkIfExistsByEmailExceptUserId(String email, Long userId);
    boolean checkIfExistsByTelegramExceptUserId(String telegram, Long userId);
    boolean checkIfExistsByLoginExceptUserId(String login, Long userId);
    boolean updateLastLoginDate(User user);
    boolean update(User user, String firstName, String lastName, String middleName, String email, String telegram, String login);
    UserHash generateUserHash(User user);
    String generateCredentialToken(User user);
    boolean changePassword(User user, String password);
    boolean checkPassword(User user, String password);
    boolean updateAvatar(User user, String avatar);
}
