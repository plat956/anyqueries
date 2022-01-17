package by.latushko.anyqueries.controller.filter.security;

import by.latushko.anyqueries.controller.command.CommandType;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static by.latushko.anyqueries.controller.command.CommandType.*;
import static by.latushko.anyqueries.controller.filter.security.AccessRole.*;

public final class RestrictedCommand {
    private static final Map<CommandType, EnumSet<AccessRole>> COMMANDS = new EnumMap<>(CommandType.class);

    static {
        COMMANDS.put(CHANGE_RATING, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(MARK_SOLUTION, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(CHANGE_QUESTION_STATUS, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(CREATE_QUESTION, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(CREATE_QUESTION_PAGE, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(EDIT_QUESTION, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(EDIT_QUESTION_PAGE, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(DELETE_QUESTION, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(CREATE_ANSWER, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(EDIT_ANSWER, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(DELETE_ANSWER, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(EDIT_PROFILE_PAGE, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(EDIT_PROFILE, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(CHANGE_PASSWORD_PAGE, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(CHANGE_PASSWORD, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(UPLOAD_AVATAR, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(LOGOUT, EnumSet.of(ADMIN, USER, MODERATOR, INACTIVE_USER));
        COMMANDS.put(CREATE_CATEGORY_PAGE, EnumSet.of(ADMIN));
        COMMANDS.put(CREATE_CATEGORY, EnumSet.of(ADMIN));
        COMMANDS.put(EDIT_CATEGORY_PAGE, EnumSet.of(ADMIN));
        COMMANDS.put(EDIT_CATEGORY, EnumSet.of(ADMIN));
        COMMANDS.put(DELETE_CATEGORY, EnumSet.of(ADMIN));
        COMMANDS.put(USERS_PAGE, EnumSet.of(ADMIN));
        COMMANDS.put(EDIT_USER_PAGE, EnumSet.of(ADMIN));
        COMMANDS.put(EDIT_USER, EnumSet.of(ADMIN));
        COMMANDS.put(DELETE_USER, EnumSet.of(ADMIN));
        COMMANDS.put(LOGIN_PAGE, EnumSet.of(GUEST));
        COMMANDS.put(LOGIN, EnumSet.of(GUEST));
        COMMANDS.put(REGISTRATION_PAGE, EnumSet.of(GUEST));
        COMMANDS.put(REGISTRATION, EnumSet.of(GUEST));
        COMMANDS.put(ACTIVATE_USER, EnumSet.of(GUEST, INACTIVE_USER));

        COMMANDS.put(REPEAT_ACTIVATION, EnumSet.of(INACTIVE_USER));
        COMMANDS.put(REPEAT_ACTIVATION_PAGE, EnumSet.of(INACTIVE_USER));
    }

    private RestrictedCommand() {
    }

    public static boolean hasAccess(CommandType commandType, AccessRole role) {
        if (commandType != null && role != null && COMMANDS.containsKey(commandType)) {
            Set<AccessRole> roles = COMMANDS.get(commandType);
            return roles.stream().anyMatch(r -> r.equals(role));
        }
        return true;
    }
}
