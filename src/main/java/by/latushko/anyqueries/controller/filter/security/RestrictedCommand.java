package by.latushko.anyqueries.controller.filter.security;

import by.latushko.anyqueries.controller.command.CommandType;
import by.latushko.anyqueries.model.entity.User;

import java.util.*;

public final class RestrictedCommand {
    private static final Map<CommandType, EnumSet<User.Role>> commands = new EnumMap<>(CommandType.class);

    static {
        commands.put(CommandType.LOGIN_PAGE, EnumSet.of(User.Role.GUEST));
        commands.put(CommandType.REGISTRATION_PAGE, EnumSet.of(User.Role.GUEST));
        commands.put(CommandType.ACTIVATE_USER, EnumSet.of(User.Role.GUEST));
        commands.put(CommandType.LOGIN, EnumSet.of(User.Role.GUEST));
        commands.put(CommandType.REGISTRATION, EnumSet.of(User.Role.GUEST));
        commands.put(CommandType.LOGOUT, EnumSet.of(User.Role.ADMIN, User.Role.USER, User.Role.MODERATOR));
        //todo add each admin pages in the future
    }

    private RestrictedCommand() {
    }

    public static boolean hasAccess(CommandType commandType, User.Role role) {
        if (commandType != null && role != null && commands.containsKey(commandType)) {
            Set<User.Role> roles = commands.get(commandType);
            return roles.stream().anyMatch(r -> r.equals(role));
        }
        return true;
    }
}
