package by.latushko.anyqueries.controller.command.identity;

public final class PagePath {
    public static final String LOGIN_PAGE = "jsp/login.jsp";
    public static final String REGISTRATION_PAGE = "jsp/registration.jsp";
    public static final String REPEAT_ACTIVATION_PAGE = "jsp/repeatActivation.jsp";
    public static final String CREATE_QUESTION_PAGE = "jsp/createQuestion.jsp";
    public static final String MAIN_PAGE = "jsp/main.jsp";
    public static final String EDIT_PROFILE_PAGE = "jsp/editProfile.jsp";
    public static final String CHANGE_PASSWORD_PAGE = "jsp/changePassword.jsp";
    public static final String REGISTRATION_URL = "/controller?command=registration_page";
    public static final String CONTROLLER_URL = "/controller";
    public static final String MAIN_URL = "/controller?command=main_page";
    public static final String LOGIN_URL = "/controller?command=login_page";
    public static final String ACTIVATE_URL = "/controller?command=activate_user";
    public static final String REPEAT_ACTIVATION_URL = "/controller?command=repeat_activation_page";
    public static final String CHANGE_PASSWORD_URL = "/controller?command=change_password_page";
    public static final String EDIT_PROFILE_URL = "/controller?command=edit_profile_page";
    public static final String CREATE_QUESTION_URL = "/controller?command=create_question_page";
    public static final String QUESTION_URL = "/controller?command=question_page&id=";

    private PagePath() {
    }
}
