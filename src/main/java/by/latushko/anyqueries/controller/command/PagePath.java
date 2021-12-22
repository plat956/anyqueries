package by.latushko.anyqueries.controller.command;

public final class PagePath {
    public static final String LOGIN_PAGE = "jsp/login.jsp";
    public static final String REGISTRATION_PAGE = "jsp/registration.jsp";
    public static final String MAIN_PAGE = "jsp/main.jsp";

    public static final String REGISTRATION_URL = "/controller?command=registration_page";
    public static final String MAIN_URL = "/controller?command=main_page";
    public static final String LOGIN_URL = "/controller?command=login_page";

    private PagePath() {
    }
}
