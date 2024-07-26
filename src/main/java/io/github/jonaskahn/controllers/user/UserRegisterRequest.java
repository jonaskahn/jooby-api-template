package io.github.jonaskahn.controllers.user;

import com.fasterxml.jackson.annotation.JsonAlias;

public record UserRegisterRequest(
        @JsonAlias({"fullName", "full_name"})
        String name,
        String email,
        String username,
        String password) {
}
