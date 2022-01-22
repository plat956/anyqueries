package by.latushko.anyqueries.util.http;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;

/**
 * The class provides opportunities for convenient work with http cookies.
 */
public final class CookieHelper {
    private static final int COOKIE_AGE_TO_ERASE = 0;

    private CookieHelper() {
    }

    /**
     * Read cookie value by key.
     *
     * @param request the http request object
     * @param key     the cookie key
     * @return the cookie value as a string
     */
    public static String readCookie(HttpServletRequest request, String key) {
        if(request != null && request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(c -> key.equals(c.getName()))
                    .map(Cookie::getValue)
                    .findAny().orElse(null);
        } else {
            return null;
        }
    }

    /**
     * Add cookie.
     *
     * @param response the http request object
     * @param key      the cookie key
     * @param value    the cookie value
     * @param maxAge   the cookie max age
     */
    public static void addCookie(HttpServletResponse response, String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * Erase cookies.
     *
     * @param request  the http request object
     * @param response the http response object
     * @param keys     the cookies keys to erase
     */
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
