package by.latushko.anyqueries.util.telegram.command.impl;

import by.latushko.anyqueries.util.telegram.ResponseMessage;
import by.latushko.anyqueries.util.telegram.command.BotCommand;
import by.latushko.anyqueries.util.telegram.command.BotCommandType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class JoinCommand implements BotCommand {
    @Override
    public BotApiMethod execute(Message inMessage) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(inMessage.getChatId()));
        message.setText(ResponseMessage.JOIN_GREETING);
        message.setReplyMarkup(buildKeyboard());
        return message;
    }

    private ReplyKeyboardMarkup buildKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        KeyboardRow row = new KeyboardRow();
        row.add(BotCommandType.ACTIVATE_ACCOUNT);
        row.add(BotCommandType.CONTACT_TO_DEVELOPER);
        keyboard.setKeyboard(List.of(row));
        return keyboard;
    }
}
