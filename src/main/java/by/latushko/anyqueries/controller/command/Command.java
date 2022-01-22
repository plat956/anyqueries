package by.latushko.anyqueries.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The Command interface.
 * Defines commands classes structure and some default common behaviour
 */
public interface Command {
    /**
     * Method to execute command
     *
     * @param request  the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @return the result of command execution
     */
    CommandResult execute(HttpServletRequest request, HttpServletResponse response);

    /**
     * Method takes and casts parameter with requested name to Long type
     *
     * @param request the HttpServletRequest object
     * @param name    the request parameter name
     * @return the parameter value casted to Long type
     */
    default Long getLongParameter(HttpServletRequest request, String name) {
        try {
            return Long.valueOf(request.getParameter(name));
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
