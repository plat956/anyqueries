package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import by.latushko.anyqueries.util.encryption.impl.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static by.latushko.anyqueries.util.AppProperty.APP_ACTIVATION_LINK_ALIVE_HOURS;

public class UserServiceImpl implements UserService {
    private static final String ACTIVATION_HASH_FAKE_SALT = "#@бЫрвалГ?";
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User createNewUser(String firstName, String lastName, String middleName, String email, String telegram, String login, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        user.setEmail(email);
        user.setTelegram(telegram);
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.ROLE_USER);
        user.setStatus(User.Status.INACTIVE);
        return user;
    }

    @Override
    public UserHash generateActivationHash(User user) {
        UserHash userHash = new UserHash();
        userHash.setUser(user);
        userHash.setHash(passwordEncoder.encode(user.getEmail() + ACTIVATION_HASH_FAKE_SALT + user.getLogin()));
        userHash.setExpires(LocalDateTime.now().plusHours(APP_ACTIVATION_LINK_ALIVE_HOURS));
        return userHash;
    }

    @Override
    public String getUserFio(User user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String middleName = user.getMiddleName();
        String fio = lastName.substring(0, 1).toUpperCase() + lastName.substring(1) + " "
                + firstName.substring(0, 1).toUpperCase() + ". "
                + middleName.substring(0, 1).toUpperCase();
        return fio;
    }
}
