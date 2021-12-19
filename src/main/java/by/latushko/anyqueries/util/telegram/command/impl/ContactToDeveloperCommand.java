package by.latushko.anyqueries.util.telegram.command.impl;

import by.latushko.anyqueries.util.telegram.TelegramBot;
import by.latushko.anyqueries.util.telegram.command.BotCommand;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ContactToDeveloperCommand implements BotCommand {
    @Override
    public BotApiMethod execute(Message inMessage) {
        SendContact contact = new SendContact();
        contact.setChatId(String.valueOf(inMessage.getChatId()));
        contact.setReplyToMessageId(inMessage.getMessageId());
        contact.setFirstName(TelegramBot.BOT_DEVELOPER_FIRST_NAME);
        contact.setLastName(TelegramBot.BOT_DEVELOPER_LAST_NAME);
        contact.setPhoneNumber(TelegramBot.BOT_DEVELOPER_PHONE);
        return contact;
    }
}
