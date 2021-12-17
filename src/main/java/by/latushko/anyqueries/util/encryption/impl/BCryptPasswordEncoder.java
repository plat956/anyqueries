package by.latushko.anyqueries.util.encryption.impl;

import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean check(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
