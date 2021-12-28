package by.latushko.anyqueries.service;

import by.latushko.anyqueries.exception.MailSenderException;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;

public interface EmailService {
    void sendActivationEmail(User user, UserHash hash) throws MailSenderException;
}
