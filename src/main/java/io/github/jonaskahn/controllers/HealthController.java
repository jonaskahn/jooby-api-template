package io.github.jonaskahn.controllers;

import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.swagger.v3.oas.annotations.Operation;

@Path("/health")
public class HealthController {

    @Operation(summary = "Health Check Endpoints")
    @GET
    public String up() {
        return "API is up!!!";
    }
}
