package by.latushko.anyqueries.util.telegram.command.impl;

import by.latushko.anyqueries.util.telegram.ResponseMessage;
import by.latushko.anyqueries.util.telegram.command.BotCommand;
import by.latushko.anyqueries.util.telegram.command.BotCommandType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
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
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(BotCommandType.ACTIVATE_ACCOUNT);
        firstRow.add(BotCommandType.HELP);
        rows.add(firstRow);
        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(BotCommandType.CONTACT_TO_DEVELOPER);
        rows.add(secondRow);
        keyboard.setKeyboard(rows);
        return keyboard;
    }
}
