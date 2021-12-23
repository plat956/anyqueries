package by.latushko.anyqueries.controller.filter.security;

import by.latushko.anyqueries.controller.command.CommandType;
import by.latushko.anyqueries.model.entity.User;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class RestrictedCommand {
    private static final Map<CommandType, List<User.Role>> commands = new EnumMap<>(CommandType.class);

    static {
        commands.put(CommandType.LOGIN_PAGE, List.of(User.Role.GUEST));
        commands.put(CommandType.REGISTRATION_PAGE, List.of(User.Role.GUEST));
        commands.put(CommandType.ACTIVATE_USER, List.of(User.Role.GUEST));
        //todo add each admin pages in the future
    }

    private RestrictedCommand() {
    }

    public static boolean hasAccess(CommandType commandType, User.Role role) {
        if (commandType != null && role != null && commands.containsKey(commandType)) {
            List<User.Role> roles = commands.get(commandType);
            return roles.stream().anyMatch(r -> r.equals(role));
        } else {
            return true;
        }
    }
}
