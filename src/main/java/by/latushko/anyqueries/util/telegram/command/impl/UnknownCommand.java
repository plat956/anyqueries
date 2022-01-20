package by.latushko.anyqueries.util.telegram.command.impl;

import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.util.telegram.command.BotCommand;
import by.latushko.anyqueries.util.telegram.command.KeyBoardBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_TELEGRAM_UNKNOWN;

public class UnknownCommand implements BotCommand {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public BotApiMethod execute(Update update) {
        Message inMessage = update.getMessage();
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(inMessage.getChatId()));
        String userLang = inMessage.getFrom().getLanguageCode();
        MessageManager manager = MessageManager.getManager(userLang);
        message.setText(manager.getMessage(MESSAGE_TELEGRAM_UNKNOWN));
        message.setReplyMarkup(KeyBoardBuilder.build(manager));
        logger.debug("User with chatId {} sent unknown query", inMessage.getChatId());
        return message;
    }
}
