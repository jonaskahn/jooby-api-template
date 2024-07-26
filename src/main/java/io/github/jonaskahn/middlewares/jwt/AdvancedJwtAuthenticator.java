package io.github.jonaskahn.middlewares.jwt;

import com.nimbusds.jwt.JWT;
import io.github.jonaskahn.constants.Jwt;
import io.github.jonaskahn.constants.RedisNameSpace;
import io.github.jonaskahn.exception.AuthorizationException;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.jwt.config.signature.SignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import redis.clients.jedis.JedisPooled;

import java.text.ParseException;

public class AdvancedJwtAuthenticator extends JwtAuthenticator {
    private final JedisPooled redis;

    public AdvancedJwtAuthenticator(JedisPooled redis, SignatureConfiguration signatureConfiguration) {
        super(signatureConfiguration);
        this.redis = redis;
    }

    @Override
    protected void createJwtProfile(TokenCredentials credentials, JWT jwt, WebContext context, SessionStore sessionStore) throws ParseException {
        var jwtId = jwt.getJWTClaimsSet().getJWTID();
        var uid = jwt.getJWTClaimsSet().getClaims().get(Jwt.Attribute.UID).toString();
        if (!redis.exists(RedisNameSpace.getUserTokenExpirationKey(uid, jwtId))) {
            throw new AuthorizationException();
        }
        super.createJwtProfile(credentials, jwt, context, sessionStore);
    }
}
