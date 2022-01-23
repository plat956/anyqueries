package by.latushko.anyqueries.util.encryption.impl;

import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class BCryptPasswordEncoderTest {

    private PasswordEncoder encoder = BCryptPasswordEncoder.getInstance();
    private String inputPassword = "qwerty1234";

    @Test
    public void testCheck() {
        String encodedPassword = encoder.encode(inputPassword);
        boolean actual = encoder.check(inputPassword, encodedPassword);
        assertTrue(actual);
    }

    @Test
    public void testCheckFalse() {
        String encodedPassword = encoder.encode(inputPassword + "rubbish");
        boolean actual = encoder.check(inputPassword, encodedPassword);
        assertFalse(actual);
    }
}