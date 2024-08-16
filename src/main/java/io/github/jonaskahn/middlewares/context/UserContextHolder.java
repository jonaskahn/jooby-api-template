package io.github.jonaskahn.middlewares.context;

import org.pac4j.core.profile.UserProfile;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class UserContextHolder {
    private static final ThreadLocal<UserProfile> threadLocalData = new ThreadLocal<>();

    public static UserProfile getCurrentUser() {
        return threadLocalData.get();
    }

    public static void setCurrentUser(UserProfile user) {
        threadLocalData.set(user);
    }

    public static Long getCurrentUserId() {
        return Optional.ofNullable(getCurrentUser())
                .map(UserProfile::getId)
                .map(Long::parseLong)
                .orElse(0L);
    }

    public static Set<String> getCurrentUserRoles() {
        return Optional.ofNullable(getCurrentUser())
                .map(UserProfile::getRoles)
                .orElse(Collections.emptySet());
    }

}
