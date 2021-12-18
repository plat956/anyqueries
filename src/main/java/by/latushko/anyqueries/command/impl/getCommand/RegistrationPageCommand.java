package by.latushko.anyqueries.command.impl.getCommand;

import by.latushko.anyqueries.command.Command;
import by.latushko.anyqueries.command.PagePath;
import by.latushko.anyqueries.command.ResponseParameter;
import jakarta.servlet.http.HttpServletRequest;

public class RegistrationPageCommand implements Command {
    @Override
    public ResponseParameter execute(HttpServletRequest request) {
        return new ResponseParameter(PagePath.REGISTRATION_PAGE, ResponseParameter.RoutingType.FORWARD);
    }
}
