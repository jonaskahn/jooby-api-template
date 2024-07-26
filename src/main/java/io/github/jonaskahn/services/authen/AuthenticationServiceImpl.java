package io.github.jonaskahn.services.authen;

import io.github.jonaskahn.constants.Jwt;
import io.github.jonaskahn.constants.RedisNameSpace;
import io.github.jonaskahn.entities.enums.Status;
import io.github.jonaskahn.repositories.UserRepository;
import io.github.jonaskahn.services.user.UserInvalidPasswordException;
import io.github.jonaskahn.services.user.UserLockedException;
import io.github.jonaskahn.services.user.UserNotFoundException;
import io.hypersistence.tsid.TSID;
import io.jooby.Context;
import io.jooby.Environment;
import jakarta.inject.Inject;
import one.util.streamex.StreamEx;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.core.profile.definition.CommonProfileDefinition;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import redis.clients.jedis.JedisPooled;

import java.util.Date;
import java.util.Optional;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;
    private final Context context;
    private final JedisPooled redis;
    private final JwtGenerator jwtGenerator;

    @Inject
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, Environment environment, Context context, JedisPooled redis) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
        this.context = context;
        this.redis = redis;
        this.jwtGenerator = new JwtGenerator(
                new SecretSignatureConfiguration(environment.getConfig().getString("jwt.salt"))
        );
    }

    @Override
    public String generateToken(String username, String password, Boolean increaseExpiration) {
        var user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(UserNotFoundException::new);
        if (user.getStatus() == Status.LOCK) {
            throw new UserLockedException();
        }
        if (user.getStatus() == Status.DELETED || user.getStatus() == Status.INACTIVATED) {
            throw new UserNotFoundException();
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserInvalidPasswordException();
        }
        var userProfile = new CommonProfile();
        userProfile.setId(user.getId().toString());
        userProfile.setRoles(StreamEx.of(user.getRoles()).toSet());
        var jid = TSID.fast().toString();
        userProfile.addAttribute(Jwt.Attribute.JTI, jid);
        userProfile.addAttribute(Jwt.Attribute.UID, user.getPreferredUsername().toString());

        var initialExpirationTime = Optional.ofNullable(environment.getConfig().getString("jwt.expiration"))
                .map(Long::parseLong)
                .orElse(3600L);

        var expirationTimes = increaseExpiration ? (initialExpirationTime + 60 * 60 * 24 * 15) : initialExpirationTime;
        var expirationDate = new Date(new Date().getTime() + expirationTimes * 1000);
        userProfile.addAttribute(Jwt.Attribute.EXP, expirationDate);
        userProfile.addAttribute(CommonProfileDefinition.DISPLAY_NAME, user.getFullName());
        redis.setex(RedisNameSpace.getUserTokenExpirationKey(user.getPreferredUsername().toString(), jid), expirationTimes, expirationDate.toString());
        return jwtGenerator.generate(userProfile);
    }

    @Override
    public void logout() {
        var userProfile = (UserProfile) context.getUser();
        assert userProfile != null;
        var jid = userProfile.getAttribute(Jwt.Attribute.JTI).toString();
        var uid = userProfile.getAttribute(Jwt.Attribute.UID).toString();
        redis.del(RedisNameSpace.getUserTokenExpirationKey(uid, jid));
    }
}
