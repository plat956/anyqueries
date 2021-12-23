package by.latushko.anyqueries.controller.command;

import by.latushko.anyqueries.controller.command.impl.getCommand.*;
import by.latushko.anyqueries.controller.command.impl.postCommand.ChangeLocaleCommand;
import by.latushko.anyqueries.controller.command.impl.postCommand.LoginCommand;
import by.latushko.anyqueries.controller.command.impl.postCommand.RegistrationCommand;
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
        getCommands.put(CommandType.ACTIVATE_USER, new ActivateUserCommand());
        getCommands.put(CommandType.MAIN_PAGE, new MainPageCommand());
        getCommands.put(CommandType.LIVE_SEARCH, new LiveSearchCommand());

        postCommands.put(CommandType.LOGIN, new LoginCommand());
        postCommands.put(CommandType.REGISTRATION, new RegistrationCommand());
        postCommands.put(CommandType.CHANGE_LOCALE, new ChangeLocaleCommand());
    }

    public static CommandProvider getInstance() {
        if (instance == null) {
            instance = new CommandProvider();
        }
        return instance;
    }

    public Optional<Command> getCommand(String commandName, RequestMethod method) {
        Optional<CommandType> commandType = CommandType.getByName(commandName);
        if(commandType.isEmpty()) {
            return Optional.empty();
        }
        Command command = switch (method) {
            case GET -> getCommands.get(commandType.get());
            case POST -> postCommands.get(commandType.get());
            default -> null;
        };

        return Optional.ofNullable(command);
    }
}
