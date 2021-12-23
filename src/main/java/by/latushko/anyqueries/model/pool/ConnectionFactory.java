package by.latushko.anyqueries.model.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class ConnectionFactory {
    private static final Logger logger = LogManager.getLogger();
    private static final String DB_PROPERTIES_PATH = "config/database.properties";
    private static final String DB_DRIVER;
    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASSWORD;
    static final int DB_POOL_SIZE;

    static {
        Properties properties = new Properties();
        try {
            InputStream inputStream = ConnectionPool.class.getClassLoader().getResourceAsStream(DB_PROPERTIES_PATH);
            properties.load(inputStream);
            DB_DRIVER = properties.getProperty("driver");
            DB_URL = properties.getProperty("url");
            DB_USER = properties.getProperty("username");
            DB_PASSWORD = properties.getProperty("password");
            DB_POOL_SIZE = Integer.parseInt(properties.getProperty("pool-size"));
            Class.forName(DB_DRIVER);
        } catch (IOException e) {
            logger.error("Failed to read connection pool properties from file: " + DB_PROPERTIES_PATH, e);
            throw new ExceptionInInitializerError("Failed to read connection pool properties from file: " + DB_PROPERTIES_PATH);
        } catch (ClassNotFoundException e) {
            logger.error("Failed to register database driver", e);
            throw new ExceptionInInitializerError("Failed to register database driver");
        }
    }

    private ConnectionFactory() {
    }

    static ProxyConnection getConnection() throws SQLException {
        return new ProxyConnection(DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD));
    }
}
