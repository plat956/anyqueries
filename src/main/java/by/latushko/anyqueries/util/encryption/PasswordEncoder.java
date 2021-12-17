package by.latushko.anyqueries.util.encryption;

public interface PasswordEncoder {
    String encode(String password);
    boolean check(String password, String hash);
}
