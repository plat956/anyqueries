package by.latushko.anyqueries.command;

public class Router {
    public enum RouterType {
        FORWARDING,
        REDIRECTION,
        AJAX_RESPONSE_BODY
    }

    private final String page;
    private final RouterType routerType;

    public Router(String page, RouterType routerType) {
        this.page = page;
        this.routerType = routerType;
    }

    public String getPage() {
        return page;
    }

    public RouterType getRouterType() {
        return routerType;
    }
}
