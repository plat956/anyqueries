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
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class MailSender {
    private static final Logger logger = LogManager.getLogger();
    private static final String MAIL_PROPERTIES_PATH = "config/mail.properties";
    private static MailSender instance;
    private static AtomicBoolean creator = new AtomicBoolean(false);
    private static ReentrantLock lockerSingleton = new ReentrantLock();
    private Properties properties;

    private MailSender() {
        this.properties = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(MAIL_PROPERTIES_PATH);
            this.properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to read mail properties from file: " + MAIL_PROPERTIES_PATH, e);
            throw new ExceptionInInitializerError("Failed to read mail properties from file: " + MAIL_PROPERTIES_PATH);
        }
    }

    public static MailSender getInstance(){
        if(!creator.get()){
            try{
                lockerSingleton.lock();
                if(instance == null){
                    instance = new MailSender();
                    creator.set(true);
                }
            } finally {
                lockerSingleton.unlock();
            }
        }
        return instance;
    }

    public void send(String sendTo, String subject, String text) throws MailSenderException {
        try {
            MimeMessage message = initMessage(sendTo, subject, text);
            Transport.send(message);
        } catch (AddressException e) {
            throw new MailSenderException("Invalid address: " + sendTo, e);
        } catch (MessagingException e) {
            throw new MailSenderException("Error generating or sending message", e);
        }
    }

    private MimeMessage initMessage(String sendTo, String subject, String text) throws MessagingException {
        String email = properties.getProperty("mail.user.email");
        String name = properties.getProperty("mail.user.name");
        String contentType = properties.getProperty("mail.content.type");
        Session mailSession = SessionFactory.createSession(properties);
        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject(subject);
        message.setContent(text, contentType);
        InternetAddress senderAddress;
        try {
            senderAddress = new InternetAddress(email, name);
        } catch (UnsupportedEncodingException e) {
            logger.error("Unsupported sender name encoding in property \"mail.user.name\", check the file: " + MAIL_PROPERTIES_PATH);
            senderAddress = new InternetAddress(email);
        }
        message.setFrom(senderAddress);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
        return message;
    }
}
