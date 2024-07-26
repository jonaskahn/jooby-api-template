package io.github.jonaskahn.constants;

public class RedisNameSpace {
    private static final String USER_TOKEN_EXPIRATION = "UserTokenExpiration";

    public static String getUserTokenExpirationKey(String preferredUsername, String jid) {
        return USER_TOKEN_EXPIRATION + ":" + preferredUsername + ":" + jid;
    }
}
