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
        COMMANDS.put(LOGIN_PAGE, EnumSet.of(GUEST));
        COMMANDS.put(REGISTRATION_PAGE, EnumSet.of(GUEST));
        COMMANDS.put(ACTIVATE_USER, EnumSet.of(GUEST, INACTIVE_USER));
        COMMANDS.put(LOGIN, EnumSet.of(GUEST));
        COMMANDS.put(REGISTRATION, EnumSet.of(GUEST));
        COMMANDS.put(LOGOUT, EnumSet.of(ADMIN, USER, INACTIVE_USER, MODERATOR));
        COMMANDS.put(REPEAT_ACTIVATION, EnumSet.of(INACTIVE_USER));
        COMMANDS.put(REPEAT_ACTIVATION_PAGE, EnumSet.of(INACTIVE_USER));
        COMMANDS.put(EDIT_PROFILE_PAGE, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(EDIT_PROFILE, EnumSet.of(ADMIN, USER, MODERATOR));
        COMMANDS.put(UPLOAD_AVATAR, EnumSet.of(ADMIN, USER, MODERATOR));
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
