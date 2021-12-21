package by.latushko.anyqueries.util.telegram;

public final class ResponseMessage {
    public static final String ACTIVATION_SUCCESSFUL = "Activation successful";
    public static final String JOIN_GREETING = "Hello \uD83D\uDC4B\nI'm glad to see you. Please, choose whatever you want :)";
    public static final String HELP_FAQ = """
            If you can't activate account because of put a wrong telegram username to the registration field on the web application, 
            you have to repeat registration one more time using the same login but correct telegram username""";
    public static final String BAD_QUERY = "Sorry, I don't know what you want";

    private ResponseMessage() {
    }
}
