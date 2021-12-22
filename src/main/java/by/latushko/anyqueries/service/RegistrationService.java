package by.latushko.anyqueries.service;

public interface RegistrationService {
    boolean registerUser(String firstName, String lastName, String middleName, String confirmationType,
                         String email, String telegram, String login, String password);
    boolean activateUserByHash(String hash);
    boolean activateUserByTelegramAccount(String account);
}
