package by.latushko.anyqueries.util.encryption.impl;

import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;

public final class BCryptPasswordEncoder implements PasswordEncoder {
    private static BCryptPasswordEncoder instance;

    private BCryptPasswordEncoder() {
    }

    public static BCryptPasswordEncoder getInstance() {
        if (instance == null) {
            instance = new BCryptPasswordEncoder();
        }
        return instance;
    }

    @Override
    public String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean check(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
