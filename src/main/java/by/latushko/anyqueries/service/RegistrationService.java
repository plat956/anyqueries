package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.util.i18n.MessageManager;

import java.util.Optional;

public interface RegistrationService {
    boolean registerUser(String firstName, String lastName, String middleName, boolean sendLink,
                         String email, String telegram, String login, String password, MessageManager manager);
    boolean updateRegistrationData(User user, String email, String telegram, boolean sendLink, MessageManager manager);
    Optional<User> activateUserByHash(String hash);
    boolean activateUserByTelegramAccount(String account);
}
