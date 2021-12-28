package by.latushko.anyqueries.util.telegram.command.impl;

import by.latushko.anyqueries.util.telegram.command.BotCommand;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.objects.Message;

import static by.latushko.anyqueries.util.AppProperty.*;

public class ContactToDeveloperCommand implements BotCommand {
    @Override
    public BotApiMethod execute(Message inMessage) {
        SendContact contact = new SendContact();
        contact.setChatId(String.valueOf(inMessage.getChatId()));
        contact.setReplyToMessageId(inMessage.getMessageId());
        contact.setFirstName(APP_DEVELOPER_FIRST_NAME);
        contact.setLastName(APP_DEVELOPER_LAST_NAME);
        contact.setPhoneNumber(APP_DEVELOPER_PHONE);
        return contact;
    }
}
