package io.github.jonaskahn.middlewares.role;

import io.jooby.Context;
import io.jooby.exception.ForbiddenException;
import jakarta.inject.Inject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.pac4j.core.profile.UserProfile;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PUBLIC, onConstructor = @__({@Inject}))
public class AccessVerifierImpl implements AccessVerifier {
    private Context context;

    @Override
    public boolean hasAnyRoles(String... roles) {
        var rolesAsList = Arrays.asList(roles);
        return Optional.ofNullable(context.getUser())
                .map(UserProfile.class::cast)
                .map(UserProfile::getRoles)
                .stream()
                .flatMap(Set::stream)
                .anyMatch(rolesAsList::contains);
    }

    @Override
    public void requireAnyRoles(String... roles) {
        if (!hasAnyRoles(roles)) {
            throw new ForbiddenException();
        }
    }
}
