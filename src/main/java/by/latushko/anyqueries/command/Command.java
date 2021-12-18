package by.latushko.anyqueries.command;

import jakarta.servlet.http.HttpServletRequest;

public interface Command {
    ResponseParameter execute(HttpServletRequest request);
}
