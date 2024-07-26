package io.github.jonaskahn.controllers.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

public record GenerateTokenRequest(
        @JsonAlias("email")
        @NotNull(message = "app.auth.validation.username")
        String username,
        @NotNull(message = "app.auth.validation.password")
        String password,
        Boolean rememberMe
) {
}
