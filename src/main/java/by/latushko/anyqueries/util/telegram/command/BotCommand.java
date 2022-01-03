package by.latushko.anyqueries.util.telegram.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotCommand {
    BotApiMethod execute(Update update);
}
