package io.github.jonaskahn.controllers.auth;

import io.github.jonaskahn.middlewares.validate.BeanValidator;
import io.github.jonaskahn.services.authen.AuthenticationService;
import io.jooby.annotation.DELETE;
import io.jooby.annotation.POST;
import io.jooby.annotation.Path;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Path("")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final BeanValidator validator;

    @POST("/auth/generate-token")
    public String generateToken(GenerateTokenRequest request) {
        validator.validate(request);
        return authenticationService.generateToken(request.username(), request.password(), request.rememberMe());
    }

    @DELETE("/secure/auth/logout")
    public void logout() {
        authenticationService.logout();
    }
}
