package by.latushko.anyqueries.command;

import by.latushko.anyqueries.command.impl.LoginActionCommand;
import by.latushko.anyqueries.command.impl.LoginPageCommand;
import by.latushko.anyqueries.command.impl.MainPageCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandProvider {
    private static CommandProvider instance;
    private final Map<CommandType, Command> commands = new HashMap<>();

    private CommandProvider() {
        commands.put(CommandType.LOGIN_PAGE, new LoginPageCommand());
        commands.put(CommandType.LOGIN_ACTION, new LoginActionCommand());
        commands.put(CommandType.MAIN_PAGE, new MainPageCommand());
    }

    public static CommandProvider getInstance() {
        if (instance == null) {
            instance = new CommandProvider();
        }
        return instance;
    }

    public Optional<Command> getCommand(String commandName, RequestMethod method) {
        if (commandName == null) {
            return Optional.empty();
        }

        CommandType commandType;
        try {
            commandType = CommandType.valueOf(commandName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }

        if(commandType.getMethod().equals(method)) {
            return Optional.of(commands.get(commandType));
        } else {
            return Optional.empty();
        }
    }
}
