package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface UserService {
    User createNewUser(String firstName, String lastName, String middleName, String email,
                       String telegram, String login, String password);
    UserHash generateActivationHash(User user);
    String getUserFio(User user);
    boolean authorize(User user, HttpServletRequest request, HttpServletResponse response);
    Optional<User> findUserByLogin(String login);
    Optional<User> findUserByCredentialKey(String key);
}
