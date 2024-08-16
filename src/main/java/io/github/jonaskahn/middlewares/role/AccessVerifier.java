package io.github.jonaskahn.middlewares.role;

import io.github.jonaskahn.middlewares.context.UserContextHolder;
import io.jooby.exception.ForbiddenException;
import org.pac4j.core.profile.UserProfile;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public final class AccessVerifier {

    public static boolean hasRole(String role) {
        return hasAnyRoles(role);
    }

    public static boolean hasAnyRoles(String... roles) {
        var rolesAsList = Arrays.asList(roles);
        return Optional.ofNullable(UserContextHolder.getCurrentUser())
                .map(UserProfile.class::cast)
                .map(UserProfile::getRoles)
                .stream()
                .flatMap(Set::stream)
                .anyMatch(rolesAsList::contains);
    }

    public static void requireRole(String role) {
        requireAnyRoles(role);
    }

    public static void requireAnyRoles(String... roles) {
        if (!hasAnyRoles(roles)) {
            throw new ForbiddenException();
        }
    }
}
