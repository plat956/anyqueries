package by.latushko.anyqueries.controller.command;

public class PreparedResponse {
    public enum RoutingType {
        FORWARD,
        REDIRECT,
        RESPOND_WITH_JSON
    }

    private final String page;
    private final RoutingType routingType;

    public PreparedResponse(String page, RoutingType routingType) {
        this.page = page;
        this.routingType = routingType;
    }

    public String getPage() {
        return page;
    }

    public RoutingType getRoutingType() {
        return routingType;
    }
}
