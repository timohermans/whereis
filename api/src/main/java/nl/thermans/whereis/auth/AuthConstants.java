package nl.thermans.whereis.auth;

public class AuthConstants {
    public static final String SECRET = "THIS_SHOULD_BE_IN_ENV_VARS";
    public static final long EXPIRATION_TIME = 900_000; // 15 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/auth/sign-up";
    public static final String SIGN_IN_URL = "/api/auth/sign-in";
}
