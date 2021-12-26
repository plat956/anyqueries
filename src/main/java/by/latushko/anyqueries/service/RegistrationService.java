package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.User;

import java.util.Optional;

public interface RegistrationService {
    boolean registerUser(String firstName, String lastName, String middleName, String confirmationType,
                         String email, String telegram, String login, String password);
    Optional<User> activateUserByHash(String hash);
    boolean activateUserByTelegramAccount(String account);
}
