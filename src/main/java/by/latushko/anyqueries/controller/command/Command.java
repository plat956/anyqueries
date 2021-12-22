package by.latushko.anyqueries.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Command {
    PreparedResponse execute(HttpServletRequest request, HttpServletResponse response);
}
