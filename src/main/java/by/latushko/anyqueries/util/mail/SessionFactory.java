package by.latushko.anyqueries.util.mail;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

import static by.latushko.anyqueries.util.mail.MailSender.USER_EMAIL_PROPERTY;
import static by.latushko.anyqueries.util.mail.MailSender.USER_PASSWORD_PROPERTY;

class SessionFactory {
    static Session createSession(Properties configProperties) {
        String userName = configProperties.getProperty(USER_EMAIL_PROPERTY);
        String userPassword = configProperties.getProperty(USER_PASSWORD_PROPERTY);
        return Session.getDefaultInstance(configProperties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, userPassword);
                    }
                });
    }
}
