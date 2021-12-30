package by.latushko.anyqueries.controller.filter.security;

import by.latushko.anyqueries.controller.command.CommandType;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static by.latushko.anyqueries.controller.command.CommandType.*;
import static by.latushko.anyqueries.controller.filter.security.AccessRole.*;

public final class RestrictedCommand {
    private static final Map<CommandType, EnumSet<AccessRole>> commands = new EnumMap<>(CommandType.class);

    static {
        commands.put(LOGIN_PAGE, EnumSet.of(GUEST));
        commands.put(REGISTRATION_PAGE, EnumSet.of(GUEST));
        commands.put(ACTIVATE_USER, EnumSet.of(GUEST, INACTIVE_USER));
        commands.put(LOGIN, EnumSet.of(GUEST));
        commands.put(REGISTRATION, EnumSet.of(GUEST));
        commands.put(LOGOUT, EnumSet.of(ADMIN, USER, INACTIVE_USER, MODERATOR));
        commands.put(REPEAT_ACTIVATION, EnumSet.of(INACTIVE_USER));
        commands.put(REPEAT_ACTIVATION_PAGE, EnumSet.of(INACTIVE_USER));
        commands.put(EDIT_PROFILE_PAGE, EnumSet.of(ADMIN, USER, MODERATOR));
        commands.put(EDIT_PROFILE, EnumSet.of(ADMIN, USER, MODERATOR));
        commands.put(UPLOAD_AVATAR, EnumSet.of(ADMIN, USER, MODERATOR));
        //todo add each admin pages in the future
    }

    private RestrictedCommand() {
    }

    public static boolean hasAccess(CommandType commandType, AccessRole role) {
        if (commandType != null && role != null && commands.containsKey(commandType)) {
            Set<AccessRole> roles = commands.get(commandType);
            return roles.stream().anyMatch(r -> r.equals(role));
        }
        return true;
    }
}
