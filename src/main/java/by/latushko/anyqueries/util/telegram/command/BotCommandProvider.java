package by.latushko.anyqueries.util.telegram.command;

import by.latushko.anyqueries.util.telegram.command.impl.ActivateAccountCommand;
import by.latushko.anyqueries.util.telegram.command.impl.ContactToDeveloperCommand;
import by.latushko.anyqueries.util.telegram.command.impl.JoinCommand;
import by.latushko.anyqueries.util.telegram.command.impl.UnknownCommand;

import java.util.HashMap;
import java.util.Map;

public class BotCommandProvider {
    private static BotCommandProvider instance;
    private final Map<String, BotCommand> commands = new HashMap<>();

    private BotCommandProvider() {
        commands.put(BotCommandType.JOIN_COMMAND, new JoinCommand());
        commands.put(BotCommandType.ACTIVATE_ACCOUNT, new ActivateAccountCommand());
        commands.put(BotCommandType.CONTACT_TO_DEVELOPER, new ContactToDeveloperCommand());
        commands.put(BotCommandType.UNKNOWN_COMMAND, new UnknownCommand());
    }

    public static BotCommandProvider getInstance() {
        if (instance == null) {
            instance = new BotCommandProvider();
        }
        return instance;
    }

    public BotCommand getCommand(String commandName) {
        BotCommand command;
        if(commands.containsKey(commandName)) {
            command = commands.get(commandName);
        } else {
            command = commands.get(BotCommandType.UNKNOWN_COMMAND);
        }
        return command;
    }
}
