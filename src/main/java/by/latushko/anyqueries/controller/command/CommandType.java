package by.latushko.anyqueries.controller.command;

import java.util.Optional;

public enum CommandType {
    LOGIN_PAGE,
    EDIT_PROFILE_PAGE,
    QUESTIONS_PAGE,
    REGISTRATION_PAGE,
    ACTIVATE_USER,
    REPEAT_ACTIVATION,
    REPEAT_ACTIVATION_PAGE,
    CREATE_QUESTION_PAGE,
    CREATE_QUESTION,
    DELETE_QUESTION,
    CHANGE_PASSWORD_PAGE,
    BAD_BROWSER_PAGE,
    SHOW_IMAGE,
    LOGIN,
    EDIT_PROFILE,
    CHANGE_PASSWORD,
    UPLOAD_AVATAR,
    LOGOUT,
    REGISTRATION,
    CHANGE_LOCALE,
    PROFILE_PAGE,
    EDIT_QUESTION_PAGE,
    LIVE_SEARCH,
    EDIT_QUESTION,
    CATEGORIES_PAGE,
    CREATE_CATEGORY_PAGE,
    CREATE_CATEGORY,
    EDIT_CATEGORY,
    EDIT_CATEGORY_PAGE,
    DELETE_CATEGORY,
    USERS_PAGE,
    QUESTION_PAGE,
    DOWNLOAD,
    CHANGE_RATING,
    MARK_SOLUTION,
    CREATE_ANSWER,
    DELETE_ANSWER,
    EDIT_ANSWER,
    DELETE_USER,
    EDIT_USER_PAGE,
    EDIT_USER;

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
