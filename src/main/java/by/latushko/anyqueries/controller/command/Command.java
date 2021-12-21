package by.latushko.anyqueries.controller.command;

import jakarta.servlet.http.HttpServletRequest;

public interface Command {
    PreparedResponse execute(HttpServletRequest request);
}
