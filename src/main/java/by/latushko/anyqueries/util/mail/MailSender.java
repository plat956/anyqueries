package by.latushko.anyqueries.util.mail;

import by.latushko.anyqueries.exception.MailSenderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static by.latushko.anyqueries.util.AppProperty.APP_NAME;
import static by.latushko.anyqueries.util.i18n.MessageManager.SPACE_CHARACTER;

public class MailSender {
    private static final Logger logger = LogManager.getLogger();
    private static final String MAIL_PROPERTIES_PATH = "config/mail.properties";
    private static final String USER_NAME_PROPERTY = "mail.user.name";
    private static final String CONTENT_TYPE_PROPERTY = "mail.content.type";
    private static final String USER_EMAIL;
    private static final String USER_NAME;
    private static final String CONTENT_TYPE;
    private static MailSender instance;
    private static final Properties properties;
    static final String USER_EMAIL_PROPERTY = "mail.user.email";
    static final String USER_PASSWORD_PROPERTY = "mail.user.password";

    static {
        properties = new Properties();
        try {
            InputStream inputStream = MailSender.class.getClassLoader().getResourceAsStream(MAIL_PROPERTIES_PATH);
            properties.load(inputStream);
            USER_EMAIL = properties.getProperty(USER_EMAIL_PROPERTY);
            USER_NAME = APP_NAME + SPACE_CHARACTER + properties.getProperty(USER_NAME_PROPERTY);
            CONTENT_TYPE = properties.getProperty(CONTENT_TYPE_PROPERTY);
        } catch (IOException e) {
            logger.fatal("Failed to read mail properties from file: {}", MAIL_PROPERTIES_PATH, e);
            throw new ExceptionInInitializerError("Failed to read mail properties from file: " + MAIL_PROPERTIES_PATH);
        }
    }

    private MailSender() {
    }

    public static MailSender getInstance(){
        if(instance == null){
            instance = new MailSender();
        }
        return instance;
    }

    public void send(String sendTo, String subject, String text) throws MailSenderException {
        try {
            MimeMessage message = initMessage(sendTo, subject, text);
            Transport.send(message);
            logger.info("Email with subject {} sent to {}", subject, sendTo);
        } catch (AddressException e) {
            logger.error("Invalid recipient emial address: {}", sendTo, e);
            throw new MailSenderException("Invalid address: " + sendTo, e);
        } catch (MessagingException e) {
            logger.error("Error generating or sending message", e);
            throw new MailSenderException("Error generating or sending message", e);
        }
    }

    private MimeMessage initMessage(String sendTo, String subject, String text) throws MessagingException {
        Session mailSession = SessionFactory.createSession(properties);
        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject(subject, StandardCharsets.UTF_8.name());
        message.setContent(text, CONTENT_TYPE);
        InternetAddress senderAddress;
        try {
            senderAddress = new InternetAddress(USER_EMAIL, USER_NAME);
        } catch (UnsupportedEncodingException e) {
            logger.error("Unsupported sender name encoding in property {}, check the file: {}", USER_NAME_PROPERTY, MAIL_PROPERTIES_PATH);
            senderAddress = new InternetAddress(USER_EMAIL);
        }
        message.setFrom(senderAddress);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
        return message;
    }
}
