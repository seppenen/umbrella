package org.umbrella.utils;

import io.jsonwebtoken.Jwts;


public class JwtUtils {

    private static final String USER_PRINCIPAL_CLAIM = "principal";

    public static String extractUserName(String token) {
        return Jwts.parserBuilder()
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(USER_PRINCIPAL_CLAIM)
                .toString();
    }
}
