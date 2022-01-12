package by.latushko.anyqueries.controller.command;

public record CommandResult(String page, RoutingType routingType) {
    public enum RoutingType {
        FORWARD,
        REDIRECT,
        DATA,
        FILE
    }
}
