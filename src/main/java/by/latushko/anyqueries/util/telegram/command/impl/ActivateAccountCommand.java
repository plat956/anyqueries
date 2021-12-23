package by.latushko.anyqueries.util.telegram.command.impl;

import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.impl.RegistrationServiceImpl;
import by.latushko.anyqueries.util.telegram.ResponseMessage;
import by.latushko.anyqueries.util.telegram.command.BotCommand;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ActivateAccountCommand implements BotCommand {
    @Override
    public BotApiMethod execute(Message inMessage) {
        SendMessage outMessage = new SendMessage();
        outMessage.setReplyToMessageId(inMessage.getMessageId());
        outMessage.setChatId(String.valueOf(inMessage.getChatId()));

        String account = inMessage.getChat().getUserName();
        RegistrationService registrationService = RegistrationServiceImpl.getInstance();
        boolean result = registrationService.activateUserByTelegramAccount(account);
        if(result) {
            outMessage.setText(ResponseMessage.ACTIVATION_SUCCESSFUL);
        } else {
            outMessage.setText(ResponseMessage.ACTIVATION_FAILED);
        }
        return outMessage;
    }
}
