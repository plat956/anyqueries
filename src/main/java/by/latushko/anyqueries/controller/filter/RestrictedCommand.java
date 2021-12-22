package by.latushko.anyqueries.controller.filter;

import by.latushko.anyqueries.controller.command.CommandType;
import by.latushko.anyqueries.model.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestrictedCommand {
    public static final Map<CommandType, List<User.Role>> commands = new HashMap<>();

    static {
        //commands.put(CommandType.LOGIN_PAGE, List.of(User.Role.ROLE_ADMIN));
    }
}
