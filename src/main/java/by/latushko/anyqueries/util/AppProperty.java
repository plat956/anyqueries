package by.latushko.anyqueries.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public final class AppProperty {
    private static final Logger logger = LogManager.getLogger();
    private static final String APP_PARAMETER_PATH = "config/application.properties";
    private static final String APP_NAME_PARAMETER = "app.name";
    private static final String APP_HOST_PARAMETER = "app.host";
    private static final String APP_ACTIVATION_LINK_ALIVE_HOURS_PARAMETER = "app.activation.link.alive";
    private static final String APP_COOKIE_ALIVE_DAYS_PARAMETER = "app.cookie.alive";
    private static final String APP_TELEGRAM_LINK_HOST_PARAMETER = "app.telegram.link.host";
    private static final String APP_DEVELOPER_FIRST_NAME_PARAMETER = "app.developer.firstName";
    private static final String APP_DEVELOPER_LAST_NAME_PARAMETER = "app.developer.lastName";
    private static final String APP_DEVELOPER_TELEGRAM_PARAMETER = "app.developer.telegram";
    private static final String APP_DEVELOPER_PHONE_PARAMETER = "app.developer.phone";
    private static final String APP_UPLOAD_AVATAR_EXTENSIONS_PARAMETER = "app.upload.avatar.extensions";
    private static final String APP_ATTACHMENT_SIZE_PARAMETER = "app.attachment.size";
    private static final String APP_ATTACHMENT_COUNT_PARAMETER = "app.attachment.count";
    private static final String APP_UPLOAD_DIR_PARAMETER = "app.upload.dir";
    private static final String APP_QUESTION_MAXLENGTH_PARAMETER = "app.question.maxlength";
    private static final String APP_ANSWER_MAXLENGTH_PARAMETER = "app.answer.maxlength";
    private static final String APP_RECORDS_PER_PAGE_PARAMETER = "app.records.per.page";
    private static final String APP_SEARCH_PREDICTIONS_LIMIT_PARAMETER = "app.search.predictions.limit";
    private static final String APP_SEARCH_QUERY_MAXLENGTH_PARAMETER = "app.search.query.maxlength";
    private static final String EXTENSION_DELIMITER = ",";
    public static final String APP_NAME;
    public static final String APP_HOST;
    public static final Integer APP_ACTIVATION_LINK_ALIVE_HOURS;
    public static final Integer APP_COOKIE_ALIVE_SECONDS;
    public static final String APP_TELEGRAM_LINK_HOST;
    public static final String APP_DEVELOPER_FIRST_NAME;
    public static final String APP_DEVELOPER_LAST_NAME;
    public static final String APP_DEVELOPER_TELEGRAM;
    public static final String APP_DEVELOPER_PHONE;
    public static final List<String> APP_UPLOAD_AVATAR_EXTENSIONS;
    public static final String APP_UPLOAD_DIR;
    public static final Integer APP_ATTACHMENT_SIZE;
    public static final Integer APP_ATTACHMENT_COUNT;
    public static final Integer APP_QUESTION_MAXLENGTH;
    public static final Integer APP_ANSWER_MAXLENGTH;
    public static final Integer APP_RECORDS_PER_PAGE;
    public static final Integer APP_SEARCH_PREDICTIONS_LIMIT;
    public static final Integer APP_SEARCH_QUERY_MAXLENGTH;

    static {
        Properties properties = new Properties();
        try {
            InputStream inputStream = AppProperty.class.getClassLoader().getResourceAsStream(APP_PARAMETER_PATH);
            properties.load(inputStream);
            APP_NAME = properties.getProperty(APP_NAME_PARAMETER);
            APP_HOST = properties.getProperty(APP_HOST_PARAMETER);
            APP_ACTIVATION_LINK_ALIVE_HOURS = Integer.valueOf(properties.getProperty(APP_ACTIVATION_LINK_ALIVE_HOURS_PARAMETER));
            APP_COOKIE_ALIVE_SECONDS = Integer.valueOf(properties.getProperty(APP_COOKIE_ALIVE_DAYS_PARAMETER)) * 24 * 60 * 60;
            APP_TELEGRAM_LINK_HOST = properties.getProperty(APP_TELEGRAM_LINK_HOST_PARAMETER);
            APP_DEVELOPER_FIRST_NAME = properties.getProperty(APP_DEVELOPER_FIRST_NAME_PARAMETER);
            APP_DEVELOPER_LAST_NAME = properties.getProperty(APP_DEVELOPER_LAST_NAME_PARAMETER);
            APP_DEVELOPER_TELEGRAM = properties.getProperty(APP_DEVELOPER_TELEGRAM_PARAMETER);
            APP_DEVELOPER_PHONE = properties.getProperty(APP_DEVELOPER_PHONE_PARAMETER);
            APP_UPLOAD_AVATAR_EXTENSIONS = new ArrayList<>();
            APP_ATTACHMENT_SIZE = Integer.valueOf(properties.getProperty(APP_ATTACHMENT_SIZE_PARAMETER));
            APP_ATTACHMENT_COUNT = Integer.valueOf(properties.getProperty(APP_ATTACHMENT_COUNT_PARAMETER));
            APP_QUESTION_MAXLENGTH = Integer.valueOf(properties.getProperty(APP_QUESTION_MAXLENGTH_PARAMETER));
            APP_ANSWER_MAXLENGTH = Integer.valueOf(properties.getProperty(APP_ANSWER_MAXLENGTH_PARAMETER));
            APP_RECORDS_PER_PAGE = Integer.valueOf(properties.getProperty(APP_RECORDS_PER_PAGE_PARAMETER));
            APP_SEARCH_PREDICTIONS_LIMIT = Integer.valueOf(properties.getProperty(APP_SEARCH_PREDICTIONS_LIMIT_PARAMETER));
            APP_SEARCH_QUERY_MAXLENGTH = Integer.valueOf(properties.getProperty(APP_SEARCH_QUERY_MAXLENGTH_PARAMETER));
            String avatarExtensions = properties.getProperty(APP_UPLOAD_AVATAR_EXTENSIONS_PARAMETER);
            if(avatarExtensions != null && !avatarExtensions.isEmpty()) {
                String[] extensions = avatarExtensions.split(EXTENSION_DELIMITER);
                APP_UPLOAD_AVATAR_EXTENSIONS.addAll(List.of(extensions));
            }
            APP_UPLOAD_DIR = properties.getProperty(APP_UPLOAD_DIR_PARAMETER);
        } catch (IOException e) {
            logger.error("Failed to read application properties from file: " + APP_PARAMETER_PATH, e);
            throw new ExceptionInInitializerError("Failed to read application properties from file: " + APP_PARAMETER_PATH);
        }
    }

    private AppProperty() {
    }
}
