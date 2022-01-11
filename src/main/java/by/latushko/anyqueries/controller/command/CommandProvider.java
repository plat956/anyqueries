package by.latushko.anyqueries.controller.command;

import by.latushko.anyqueries.controller.command.impl.get.*;
import by.latushko.anyqueries.controller.command.impl.post.*;
import by.latushko.anyqueries.util.http.RequestMethod;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class CommandProvider {
    private static CommandProvider instance;
    private final Map<CommandType, Command> getCommands = new EnumMap<>(CommandType.class);
    private final Map<CommandType, Command> postCommands = new EnumMap<>(CommandType.class);

    private CommandProvider() {
        getCommands.put(CommandType.LOGIN_PAGE, new LoginPageCommand());
        getCommands.put(CommandType.EDIT_PROFILE_PAGE, new EditProfilePageCommand());
        getCommands.put(CommandType.REGISTRATION_PAGE, new RegistrationPageCommand());
        getCommands.put(CommandType.REPEAT_ACTIVATION_PAGE, new RepeatActivationPageCommand());
        getCommands.put(CommandType.ACTIVATE_USER, new ActivateUserCommand());
        getCommands.put(CommandType.QUESTIONS_PAGE, new QuestionsPageCommand());
        getCommands.put(CommandType.LIVE_SEARCH, new LiveSearchCommand());
        getCommands.put(CommandType.CHANGE_PASSWORD_PAGE, new ChangePasswordPageCommand());
        getCommands.put(CommandType.SHOW_IMAGE, new ShowImageCommand());
        getCommands.put(CommandType.CREATE_QUESTION_PAGE, new CreateQuestionPageCommand());
        getCommands.put(CommandType.BAD_BROWSER_PAGE, new BadBrowserPageCommand());
        getCommands.put(CommandType.PROFILE_PAGE, new ProfilePageCommand());
        getCommands.put(CommandType.EDIT_QUESTION_PAGE, new EditQuestionPageCommand());
        getCommands.put(CommandType.CATEGORIES_PAGE, new CategoriesPageCommand());
        getCommands.put(CommandType.CREATE_CATEGORY_PAGE, new CreateCategoryPage());
        postCommands.put(CommandType.LOGIN, new LoginCommand());
        postCommands.put(CommandType.LOGOUT, new LogoutCommand());
        postCommands.put(CommandType.REGISTRATION, new RegistrationCommand());
        postCommands.put(CommandType.CHANGE_LOCALE, new ChangeLocaleCommand());
        postCommands.put(CommandType.REPEAT_ACTIVATION, new RepeatActivationCommand());
        postCommands.put(CommandType.EDIT_PROFILE, new EditProfileCommand());
        postCommands.put(CommandType.CHANGE_PASSWORD, new ChangePasswordCommand());
        postCommands.put(CommandType.UPLOAD_AVATAR, new UploadAvatarCommand());
        postCommands.put(CommandType.CREATE_QUESTION, new CreateQuestionCommand());
        postCommands.put(CommandType.DELETE_QUESTION, new DeleteQuestionCommand());
        postCommands.put(CommandType.EDIT_QUESTION, new EditQuestionCommand());
        postCommands.put(CommandType.CREATE_CATEGORY, new CreateCategoryCommand());
    }

    public static CommandProvider getInstance() {
        if (instance == null) {
            instance = new CommandProvider();
        }
        return instance;
    }

    public Optional<Command> getCommand(String commandName, RequestMethod method) {
        Optional<CommandType> commandType = CommandType.getByName(commandName);
        if(commandType.isEmpty()) {
            return Optional.empty();
        }
        Command command = switch (method) {
            case GET -> getCommands.get(commandType.get());
            case POST -> postCommands.get(commandType.get());
            default -> null;
        };

        return Optional.ofNullable(command);
    }
}
