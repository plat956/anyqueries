package by.latushko.anyqueries.util.telegram.command;

import by.latushko.anyqueries.util.i18n.MessageManager;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static by.latushko.anyqueries.util.i18n.MessageKey.*;
import static by.latushko.anyqueries.util.telegram.command.BotCommandType.*;

public final class KeyBoardBuilder {
    public static InlineKeyboardMarkup build(MessageManager manager) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText(manager.getMessage(LABEL_TELEGRAM_ACTIVATE));
        inlineKeyboardButton1.setCallbackData(ACTIVATE_ACCOUNT);
        inlineKeyboardButton2.setText(manager.getMessage(LABEL_TELEGRAM_CONTACT));
        inlineKeyboardButton2.setCallbackData(CONTACT_TO_DEVELOPER);
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        inlineKeyboardMarkup.setKeyboard(List.of(keyboardButtonsRow1));
        return inlineKeyboardMarkup;
    }

    private KeyBoardBuilder() {
    }
}
