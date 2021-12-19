package by.latushko.anyqueries.util.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger logger = LogManager.getLogger();
    private static final String BOT_PROPERTIES_PATH = "config/telegram.properties";
    private static final String BOT_NAME_PARAMETER = "telegram.bot.name";
    private static final String BOT_TOKEN_PARAMETER = "telegram.bot.token";
    private static final String DEVELOPER_FIRST_NAME_PARAMETER = "telegram.bot.developer.firstname";
    private static final String DEVELOPER_LAST_NAME_PARAMETER = "telegram.bot.developer.lastname";
    private static final String DEVELOPER_PHONE_PARAMETER = "telegram.bot.developer.phone";
    private final Properties properties;

    public TelegramBot() {
        this.properties = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(BOT_PROPERTIES_PATH);
            this.properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to read telegram bot properties from file: " + BOT_PROPERTIES_PATH, e);
            throw new ExceptionInInitializerError("Failed to read telegram bot properties from file: " + BOT_PROPERTIES_PATH);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message inMessage = update.getMessage();


            BotApiMethod method = null;
            Long chatId = inMessage.getChatId();
            String command = inMessage.getText();
            if(command.equals("/start")) {
                String message = "Hello \uD83D\uDC4B\nI'm glad to see you. Please, choose whatever you want :)";
                SendMessage outMessage = new SendMessage();

                outMessage.setChatId(String.valueOf(inMessage.getChatId()));
                outMessage.setText(message);


                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                keyboardMarkup.setResizeKeyboard(true);
                KeyboardRow row = new KeyboardRow();
                row.add("Activate account");
                row.add("Contact to developer");
                keyboardMarkup.setKeyboard(List.of(row));
                outMessage.setReplyMarkup(keyboardMarkup);
                method = outMessage;
            }else if(command.equals("Activate account")) {
                String message = "Activation successful";
                SendMessage outMessage = new SendMessage();
                outMessage.setReplyToMessageId(inMessage.getMessageId());
                outMessage.setChatId(String.valueOf(inMessage.getChatId()));
                outMessage.setText(message);

                method = outMessage;
            } else if(command.equals("Contact to developer")) {
                SendContact contact = new SendContact();
                contact.setChatId(String.valueOf(inMessage.getChatId()));
                contact.setReplyToMessageId(inMessage.getMessageId());

                contact.setFirstName(properties.getProperty(DEVELOPER_FIRST_NAME_PARAMETER));
                contact.setLastName(properties.getProperty(DEVELOPER_LAST_NAME_PARAMETER));
                contact.setPhoneNumber(properties.getProperty(DEVELOPER_PHONE_PARAMETER));
                method = contact;
            } else {
                String message = "Sorry, I don't know what you want";
                SendMessage outMessage = new SendMessage();
                outMessage.setReplyToMessageId(inMessage.getMessageId());
                outMessage.setChatId(String.valueOf(inMessage.getChatId()));
                outMessage.setText(message);

                method = outMessage;
            }

            try {
                execute(method);
            } catch (TelegramApiException e) {
                logger.error("Failed to send message to chatId: " + chatId, e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return properties.getProperty(BOT_NAME_PARAMETER);
    }

    @Override
    public String getBotToken() {
        return properties.getProperty(BOT_TOKEN_PARAMETER);
    }
}