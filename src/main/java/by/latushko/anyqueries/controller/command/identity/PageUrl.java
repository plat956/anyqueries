package by.latushko.anyqueries.controller.command.identity;

public final class PageUrl {
    public static final String REGISTRATION_URL = "/controller?command=registration_page";
    public static final String CONTROLLER_URL = "/controller";
    public static final String QUESTIONS_URL = "/controller?command=questions_page";
    public static final String LOGIN_URL = "/controller?command=login_page";
    public static final String ACTIVATE_URL = "/controller?command=activate_user";
    public static final String REPEAT_ACTIVATION_URL = "/controller?command=repeat_activation_page";
    public static final String CHANGE_PASSWORD_URL = "/controller?command=change_password_page";
    public static final String EDIT_PROFILE_URL = "/controller?command=edit_profile_page";
    public static final String EDIT_USER_URL = "/controller?command=edit_user_page&id=";
    public static final String USERS_URL = "/controller?command=users_page";
    public static final String QUESTION_URL = "/controller?command=question_page&id=";
    public static final String CATEGORIES_URL = "/controller?command=categories_page";
    public static final String EDIT_QUESTION_URL = "/controller?command=edit_question_page&id=";
    public static final String CREATE_CATEGORY_URL = "/controller?command=create_category_page";
    public static final String EDIT_CATEGORY_URL = "/controller?command=edit_category_page&id=";

    private PageUrl() {
    }
}
