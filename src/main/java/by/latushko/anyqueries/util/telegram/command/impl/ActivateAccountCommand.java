package by.latushko.anyqueries.util.telegram.command.impl;

import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.impl.RegistrationServiceImpl;
import by.latushko.anyqueries.util.AppProperty;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.util.telegram.command.BotCommand;
import by.latushko.anyqueries.util.telegram.command.KeyBoardBuilder;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_TELEGRAM_ACTIVATION_FAIL;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_TELEGRAM_ACTIVATION_SUCCESS;

public class ActivateAccountCommand implements BotCommand {
    @Override
    public BotApiMethod execute(Update update) {
        CallbackQuery callback = update.getCallbackQuery();
        Message inMessage = callback.getMessage();
        SendMessage outMessage = new SendMessage();
        outMessage.setChatId(String.valueOf(inMessage.getChatId()));
        String account = callback.getFrom().getUserName();
        String userLang = callback.getFrom().getLanguageCode();
        MessageManager manager = MessageManager.getManager(userLang);
        RegistrationService registrationService = RegistrationServiceImpl.getInstance();
        boolean result = registrationService.activateUserByTelegramAccount(account);
        if(result) {
            outMessage.setText(manager.getMessage(MESSAGE_TELEGRAM_ACTIVATION_SUCCESS, AppProperty.APP_NAME));
        } else {
            outMessage.setText(manager.getMessage(MESSAGE_TELEGRAM_ACTIVATION_FAIL, AppProperty.APP_NAME));
        }
        outMessage.setReplyMarkup(KeyBoardBuilder.build(manager));
        return outMessage;
    }
}
