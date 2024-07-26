package io.github.jonaskahn.middlewares.role;

import com.google.inject.ImplementedBy;

@ImplementedBy(AccessVerifierImpl.class)
public interface AccessVerifier {

    default boolean hasRole(String role) {
        return hasAnyRoles(role);
    }

    boolean hasAnyRoles(String... roles);

    default void requireRole(String role) {
        requireAnyRoles(role);
    }

    void requireAnyRoles(String... roles);
}
