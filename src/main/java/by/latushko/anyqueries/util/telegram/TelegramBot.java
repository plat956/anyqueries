package by.latushko.anyqueries.util.telegram;

import by.latushko.anyqueries.util.telegram.command.BotCommand;
import by.latushko.anyqueries.util.telegram.command.BotCommandProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
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
    private static final String BOT_TOKEN;
    private static final Properties properties;
    public static final String BOT_NAME;

    static {
        properties = new Properties();
        try {
            InputStream inputStream = TelegramBot.class.getClassLoader().getResourceAsStream(BOT_PROPERTIES_PATH);
            properties.load(inputStream);
            BOT_NAME = properties.getProperty(BOT_NAME_PARAMETER);
            BOT_TOKEN = properties.getProperty(BOT_TOKEN_PARAMETER);
        } catch (IOException e) {
            logger.error("Failed to read telegram bot properties from file: " + BOT_PROPERTIES_PATH, e);
            throw new ExceptionInInitializerError("Failed to read telegram bot properties from file: " + BOT_PROPERTIES_PATH);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        String query = null;
        Long chatId = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            query = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
        } else if(update.hasCallbackQuery()){
            query = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        BotCommandProvider commandProvider = BotCommandProvider.getInstance();
        BotCommand command = commandProvider.getCommand(query);
        BotApiMethod method = command.execute(update);
        try {
            execute(method);
        } catch (TelegramApiException e) {
            logger.error("Failed to handle message from chatId: " + chatId, e);
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