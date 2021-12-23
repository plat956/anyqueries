package by.latushko.anyqueries.util.http;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Optional;

public final class CookieHelper {
    public static Optional<String> readCookie(HttpServletRequest request, String key) {
        if(request != null && request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(c -> key.equals(c.getName()))
                    .map(Cookie::getValue)
                    .findAny();
        } else {
            return Optional.empty();
        }
    }
}
