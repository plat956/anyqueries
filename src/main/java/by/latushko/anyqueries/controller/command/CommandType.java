package by.latushko.anyqueries.controller.command;

import java.util.Optional;

public enum CommandType {
    LOGIN_PAGE,
    MAIN_PAGE,
    REGISTRATION_PAGE,
    ACTIVATE_USER,
    REPEAT_ACTIVATION,
    REPEAT_ACTIVATION_PAGE,
    LOGIN,
    LOGOUT,
    REGISTRATION,
    CHANGE_LOCALE,
    LIVE_SEARCH;

    public static Optional<CommandType> getByName(String commandName) {
        if (commandName == null) {
            return Optional.empty();
        }
        Optional<CommandType> commandType;
        try {
            commandType = Optional.of(CommandType.valueOf(commandName.toUpperCase()));
        } catch (IllegalArgumentException e) {
            commandType = Optional.empty();
        }
        return commandType;
    }
}
