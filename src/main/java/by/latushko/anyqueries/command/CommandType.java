package by.latushko.anyqueries.command;

import static by.latushko.anyqueries.command.RequestMethod.GET;
import static by.latushko.anyqueries.command.RequestMethod.POST;

public enum CommandType {
    LOGIN_PAGE(GET),
    LOGIN_ACTION(POST),
    MAIN_PAGE(GET),
    REGISTRATION_PAGE(GET);

    private RequestMethod method;

    CommandType(RequestMethod method) {
        this.method = method;
    }

    public RequestMethod getMethod() {
        return method;
    }
}
