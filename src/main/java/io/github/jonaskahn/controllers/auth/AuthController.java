package io.github.jonaskahn.controllers.auth;

import io.github.jonaskahn.assistant.Response;
import io.github.jonaskahn.middlewares.validate.BeanValidator;
import io.github.jonaskahn.services.authen.AuthenticationService;
import io.jooby.annotation.DELETE;
import io.jooby.annotation.POST;
import io.jooby.annotation.Path;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Path("")
public class AuthController {
    private final AuthenticationService authenticationService;

    @POST("/auth/generate-token")
    public Response<String> generateToken(GenerateTokenRequest request) {
        BeanValidator.validate(request);
        val token = authenticationService.generateToken(request.username(), request.password(), request.rememberMe());
        return Response.ok("app.common.message.welcome", token);
    }

    @DELETE("/secure/auth/logout")
    public void logout() {
        authenticationService.logout();
    }
}
