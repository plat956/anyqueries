package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface UserService {
    User createNewUser(String firstName, String lastName, String middleName, String email,
                       String telegram, String login, String password);
    UserHash generateUserHash(User user);
    boolean authorize(User user, HttpServletRequest request, HttpServletResponse response, boolean remember, boolean manually);
    Optional<User> findUserByLogin(String login);
    Optional<User> findUserByCredentialKey(String key);
    boolean changeLocale(String lang, HttpServletResponse response);
    String getCredentialTokenSource(User user);
}
