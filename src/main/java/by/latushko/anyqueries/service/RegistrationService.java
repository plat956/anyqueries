package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.util.i18n.MessageManager;

import java.util.Optional;

/**
 * The Registration service interface.
 */
public interface RegistrationService {
    /**
     * Register user.
     *
     * @param firstName  the first name
     * @param lastName   the last name
     * @param middleName the middle name
     * @param sendLink   the send link flag
     * @param email      the email
     * @param telegram   the telegram
     * @param login      the login
     * @param password   the password
     * @param manager    the manager
     * @param url        the current app host url
     * @return the boolean, true if the user account was created successfully, otherwise false
     */
    boolean registerUser(String firstName, String lastName, String middleName, boolean sendLink, String email,
                    String telegram, String login, String password, MessageManager manager, StringBuffer url);

    /**
     * Update registration data.
     *
     * @param user     the user
     * @param email    the email
     * @param telegram the telegram
     * @param sendLink the send link
     * @param manager  the manager
     * @param url      the url
     * @return the boolean, true if the user registration data was changed successfully, otherwise false
     */
    boolean updateRegistrationData(User user, String email, String telegram, boolean sendLink,
                    MessageManager manager, StringBuffer url);

    /**
     * Activate user by hash.
     *
     * @param hash the activation hash
     * @return the optional with the activated user or empty one
     */
    Optional<User> activateUserByHash(String hash);

    /**
     * Activate user by telegram account.
     *
     * @param account the telegram account
     * @return the boolean, true if the user account was activated successfully, otherwise false
     */
    boolean activateUserByTelegramAccount(String account);
}
