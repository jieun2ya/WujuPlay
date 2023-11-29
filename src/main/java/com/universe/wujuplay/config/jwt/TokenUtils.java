package com.universe.wujuplay.config.jwt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class TokenUtils {
    public static String extractAccessTokenFromRequest(HttpServletRequest request) {
        // Implement this method to extract the access token from the request
        // You might get it from headers, cookies, or request parameters depending on your setup
        // For example, if you store the token in a cookie:
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
