package org.spring.authservice.utility;

public class TokenUtility {
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 240;
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 15;
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String TOKEN_VALID = "tokenValid";


}
