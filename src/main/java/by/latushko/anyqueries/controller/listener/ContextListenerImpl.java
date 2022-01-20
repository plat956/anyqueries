package by.latushko.anyqueries.controller.listener;

import by.latushko.anyqueries.model.pool.ConnectionPool;
import by.latushko.anyqueries.util.telegram.TelegramBot;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static by.latushko.anyqueries.util.telegram.TelegramBot.BOT_ALIVE;

@WebListener
public class ContextListenerImpl implements ServletContextListener {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ConnectionPool.getInstance();
        if(BOT_ALIVE) {
            try {
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                botsApi.registerBot(TelegramBot.getInstance());
                logger.info("Telegram bot started successfully");
            } catch (TelegramApiException e) {
                logger.fatal("Unable to start telegram bot", e);
                throw new ExceptionInInitializerError("Unable to start telegram bot");
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ConnectionPool.getInstance().destroyPool();
    }
}
