package by.latushko.anyqueries.service;

import by.latushko.anyqueries.exception.MailSenderException;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;

/**
 * The Email service interface.
 */
public interface EmailService {
    /**
     * Send activation email.
     *
     * @param user the recipient user
     * @param hash the user account activation hash
     * @throws MailSenderException if something goes wrong during send email
     */
    void sendActivationEmail(User user, UserHash hash) throws MailSenderException;
}
