package by.latushko.anyqueries.util.encryption;

/**
 * The Password encoder interface.
 */
public interface PasswordEncoder {
    /**
     * Encode password.
     *
     * @param password the input password
     * @return the encoded string
     */
    String encode(String password);

    /**
     * Check if password is correct.
     *
     * @param password the input password
     * @param hash     the hash of the password
     * @return the boolean, true if password is correct, otherwise false
     */
    boolean check(String password, String hash);
}
