package by.latushko.anyqueries.util.telegram.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotCommand {
    BotApiMethod execute(Message inMessage);
}
