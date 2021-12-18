package by.latushko.anyqueries.command;

import by.latushko.anyqueries.command.impl.getCommand.LoginPageCommand;
import by.latushko.anyqueries.command.impl.getCommand.MainPageCommand;
import by.latushko.anyqueries.command.impl.getCommand.RegistrationPageCommand;
import by.latushko.anyqueries.command.impl.postCommand.LoginCommand;
import by.latushko.anyqueries.command.impl.postCommand.RegistrationCommand;
import by.latushko.anyqueries.util.http.RequestMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandProvider {
    private static CommandProvider instance;
    private final Map<CommandType, Command> getCommands = new HashMap<>();
    private final Map<CommandType, Command> postCommands = new HashMap<>();

    private CommandProvider() {
        getCommands.put(CommandType.LOGIN_PAGE, new LoginPageCommand());
        getCommands.put(CommandType.REGISTRATION_PAGE, new RegistrationPageCommand());
        getCommands.put(CommandType.MAIN_PAGE, new MainPageCommand());
        postCommands.put(CommandType.LOGIN, new LoginCommand());
        postCommands.put(CommandType.REGISTRATION, new RegistrationCommand());
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

        Command command = switch (method) {
            case GET -> getCommands.get(commandType);
            case POST -> postCommands.get(commandType);
            default -> null;
        };

        return Optional.ofNullable(command);
    }
}
