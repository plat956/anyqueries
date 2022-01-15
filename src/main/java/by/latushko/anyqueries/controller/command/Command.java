package by.latushko.anyqueries.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Command {
    CommandResult execute(HttpServletRequest request, HttpServletResponse response);

    default Long getLongParameter(HttpServletRequest request, String name) {
        try {
            return Long.valueOf(request.getParameter(name));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
