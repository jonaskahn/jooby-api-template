package io.github.jonaskahn.controllers;

import io.github.jonaskahn.constants.Roles;
import io.github.jonaskahn.middlewares.role.AccessVerifier;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Path("/secure/test")
public class TestRoleController {

    @GET("/admin-role")
    public String testAdmin() {
        AccessVerifier.requireRole(Roles.ADMIN);
        return "ok";
    }
}
