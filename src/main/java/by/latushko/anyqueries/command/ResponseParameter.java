package by.latushko.anyqueries.command;

public class ResponseParameter {
    public enum RoutingType {
        FORWARD,
        REDIRECT,
        RESPOND_WITH_JSON
    }

    private final String page;
    private final RoutingType routingType;

    public ResponseParameter(String page, RoutingType routingType) {
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
