package by.latushko.anyqueries.util.telegram.command.impl;

import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.util.telegram.command.BotCommand;
import by.latushko.anyqueries.util.telegram.command.KeyBoardBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static by.latushko.anyqueries.util.AppProperty.*;

public class ContactToDeveloperCommand implements BotCommand {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public BotApiMethod execute(Update update) {
        CallbackQuery callback = update.getCallbackQuery();
        Message inMessage = callback.getMessage();
        SendContact contact = new SendContact();
        contact.setChatId(String.valueOf(inMessage.getChatId()));
        contact.setFirstName(APP_DEVELOPER_FIRST_NAME);
        contact.setLastName(APP_DEVELOPER_LAST_NAME);
        contact.setPhoneNumber(APP_DEVELOPER_PHONE);
        String userLang = callback.getFrom().getLanguageCode();
        MessageManager manager = MessageManager.getManager(userLang);
        contact.setReplyMarkup(KeyBoardBuilder.build(manager));
        logger.debug("User with chatId {} requested developer contacts", inMessage.getChatId());
        return contact;
    }
}
