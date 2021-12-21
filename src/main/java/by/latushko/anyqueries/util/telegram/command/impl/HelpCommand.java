package by.latushko.anyqueries.util.telegram.command.impl;

import by.latushko.anyqueries.util.telegram.ResponseMessage;
import by.latushko.anyqueries.util.telegram.command.BotCommand;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class HelpCommand implements BotCommand {
    @Override
    public BotApiMethod execute(Message inMessage) {
        SendMessage message = new SendMessage();
        message.setReplyToMessageId(inMessage.getMessageId());
        message.setChatId(String.valueOf(inMessage.getChatId()));
        message.setText(ResponseMessage.HELP_FAQ);
        return message;
    }
}
