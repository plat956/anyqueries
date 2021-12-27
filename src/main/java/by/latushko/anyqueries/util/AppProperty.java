package by.latushko.anyqueries.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppProperty {
    private static final Logger logger = LogManager.getLogger();
    private static final String APP_PARAMETER_PATH = "config/application.properties";
    private static final String APP_HOST_PARAMETER = "app.host";
    private static final String APP_ACTIVATION_LINK_ALIVE_HOURS_PARAMETER = "app.activation.link.alive";
    private static final String APP_COOKIE_ALIVE_DAYS_PARAMETER = "app.cookie.alive";
    private static final String APP_TELEGRAM_LINK_HOST_PARAMETER = "app.telegram.link.host";
    public static final String APP_HOST;
    public static final Integer APP_ACTIVATION_LINK_ALIVE_HOURS;
    public static final Integer APP_COOKIE_ALIVE_SECONDS;
    public static final String APP_TELEGRAM_LINK_HOST;

    static {
        Properties properties = new Properties();
        try {
            InputStream inputStream = AppProperty.class.getClassLoader().getResourceAsStream(APP_PARAMETER_PATH);
            properties.load(inputStream);
            APP_HOST = properties.getProperty(APP_HOST_PARAMETER);
            APP_ACTIVATION_LINK_ALIVE_HOURS = Integer.valueOf(properties.getProperty(APP_ACTIVATION_LINK_ALIVE_HOURS_PARAMETER));
            APP_COOKIE_ALIVE_SECONDS = Integer.valueOf(properties.getProperty(APP_COOKIE_ALIVE_DAYS_PARAMETER)) * 24 * 60 * 60;
            APP_TELEGRAM_LINK_HOST = properties.getProperty(APP_TELEGRAM_LINK_HOST_PARAMETER);
        } catch (IOException e) {
            logger.error("Failed to read application properties from file: " + APP_PARAMETER_PATH, e);
            throw new ExceptionInInitializerError("Failed to read application properties from file: " + APP_PARAMETER_PATH);
        }
    }

    private AppProperty() {
    }
}
