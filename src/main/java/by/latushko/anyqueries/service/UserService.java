package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;

import java.util.Optional;

public interface UserService {
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
}
