package by.latushko.anyqueries.controller.filter.security;

import by.latushko.anyqueries.controller.command.CommandType;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class RestrictedCommand {
    private static final Map<CommandType, EnumSet<AccessRole>> commands = new EnumMap<>(CommandType.class);

    static {
        commands.put(CommandType.LOGIN_PAGE, EnumSet.of(AccessRole.GUEST));
        commands.put(CommandType.REGISTRATION_PAGE, EnumSet.of(AccessRole.GUEST));
        commands.put(CommandType.ACTIVATE_USER, EnumSet.of(AccessRole.GUEST, AccessRole.INACTIVE_USER));
        commands.put(CommandType.LOGIN, EnumSet.of(AccessRole.GUEST));
        commands.put(CommandType.REGISTRATION, EnumSet.of(AccessRole.GUEST));
        commands.put(CommandType.LOGOUT, EnumSet.of(AccessRole.ADMIN, AccessRole.USER, AccessRole.INACTIVE_USER, AccessRole.MODERATOR));
        commands.put(CommandType.REPEAT_ACTIVATION, EnumSet.of(AccessRole.INACTIVE_USER));
        commands.put(CommandType.REPEAT_ACTIVATION_PAGE, EnumSet.of(AccessRole.INACTIVE_USER));
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
