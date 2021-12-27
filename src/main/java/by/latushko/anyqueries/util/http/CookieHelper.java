package by.latushko.anyqueries.util.http;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CookieHelper {
    private static final int COOKIE_AGE_TO_ERASE = 0;

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

    public static void addCookie(HttpServletResponse response, String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void eraseCookie(HttpServletRequest request, HttpServletResponse response, String... keys) {
        if(request != null && request.getCookies() != null) {
            List<String> keyList = List.of(keys);
            Arrays.stream(request.getCookies())
                    .filter(c -> keyList.contains(c.getName()))
                    .forEach(c -> {
                        c.setMaxAge(COOKIE_AGE_TO_ERASE);
                        response.addCookie(c);
                    });
        }
    }
}
