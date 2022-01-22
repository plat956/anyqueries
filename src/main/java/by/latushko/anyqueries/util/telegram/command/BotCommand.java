package by.latushko.anyqueries.util.telegram.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * The Bot command interface.
 */
public interface BotCommand {
    /**
     * Execute bot command.
     *
     * @param update the update object containing user request information
     * @return the bot api method
     */
    BotApiMethod execute(Update update);
}
