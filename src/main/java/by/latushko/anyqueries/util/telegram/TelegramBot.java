package by.latushko.anyqueries.util.telegram;

import by.latushko.anyqueries.command.CommandProvider;
import by.latushko.anyqueries.util.telegram.command.BotCommand;
import by.latushko.anyqueries.util.telegram.command.BotCommandProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger logger = LogManager.getLogger();
    private static final String BOT_PROPERTIES_PATH = "config/telegram.properties";
    private static final String BOT_NAME_PARAMETER = "telegram.bot.name";
    private static final String BOT_TOKEN_PARAMETER = "telegram.bot.token";
    private static final String BOT_DEVELOPER_FIRST_NAME_PARAMETER = "telegram.bot.developer.firstname";
    private static final String BOT_DEVELOPER_LAST_NAME_PARAMETER = "telegram.bot.developer.lastname";
    private static final String BOT_DEVELOPER_PHONE_PARAMETER = "telegram.bot.developer.phone";
    private static final String BOT_TOKEN;
    private static final Properties properties;
    public static final String BOT_NAME;
    public static final String BOT_DEVELOPER_FIRST_NAME;
    public static final String BOT_DEVELOPER_LAST_NAME;
    public static final String BOT_DEVELOPER_PHONE;

    static {
        properties = new Properties();
        try {
            InputStream inputStream = TelegramBot.class.getClassLoader().getResourceAsStream(BOT_PROPERTIES_PATH);
            properties.load(inputStream);
            BOT_NAME = properties.getProperty(BOT_NAME_PARAMETER);
            BOT_TOKEN = properties.getProperty(BOT_TOKEN_PARAMETER);
            BOT_DEVELOPER_FIRST_NAME = properties.getProperty(BOT_DEVELOPER_FIRST_NAME_PARAMETER);
            BOT_DEVELOPER_LAST_NAME = properties.getProperty(BOT_DEVELOPER_LAST_NAME_PARAMETER);
            BOT_DEVELOPER_PHONE = properties.getProperty(BOT_DEVELOPER_PHONE_PARAMETER);
        } catch (IOException e) {
            logger.error("Failed to read telegram bot properties from file: " + BOT_PROPERTIES_PATH, e);
            throw new ExceptionInInitializerError("Failed to read telegram bot properties from file: " + BOT_PROPERTIES_PATH);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message inMessage = update.getMessage();
            Long chatId = inMessage.getChatId();
            String query = inMessage.getText();

            BotCommandProvider commandProvider = BotCommandProvider.getInstance();
            BotCommand command = commandProvider.getCommand(query);
            BotApiMethod method = command.execute(inMessage);

            try {
                execute(method);
            } catch (TelegramApiException e) {
                logger.error("Failed to send message to chatId: " + chatId, e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}