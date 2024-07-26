package io.github.jonaskahn.controllers.auth;

@io.jooby.annotation.Generated(AuthController.class)
public class AuthController_ implements io.jooby.MvcExtension, io.jooby.MvcFactory<AuthController> {
    protected final java.util.function.Function<io.jooby.Context, AuthController> factory;

    public AuthController_() {
      this(AuthController.class);
    }

    public AuthController_(Class<AuthController> type) {
      this(ctx -> ctx.require(type));
    }

    public AuthController_(AuthController instance) {
        this(ctx -> instance);
    }

    public AuthController_(java.util.function.Supplier<AuthController> provider) {
        this(ctx -> provider.get());
    }

    public AuthController_(java.util.function.Function<io.jooby.Context, AuthController> factory) {
        this.factory = factory;
    }

    public void install(io.jooby.Jooby app) throws Exception {
      /** See {@link AuthController#generateToken(io.github.jonaskahn.controllers.auth.GenerateTokenRequest) */
      app.post("/auth/generate-token", this::generateToken)
        .setReturnType(String.class)
        .setMvcMethod(AuthController.class.getMethod("generateToken", io.github.jonaskahn.controllers.auth.GenerateTokenRequest.class));

      /** See {@link AuthController#logout() */
      app.delete("/secure/auth/logout", this::logout)
        .setReturnType(io.jooby.StatusCode.class)
        .setMvcMethod(AuthController.class.getMethod("logout"));
    }

    public String generateToken(io.jooby.Context ctx) {
      var c = this.factory.apply(ctx);
      return c.generateToken(ctx.body(io.github.jonaskahn.controllers.auth.GenerateTokenRequest.class));
    }

    public io.jooby.StatusCode logout(io.jooby.Context ctx) {
      ctx.setResponseCode(io.jooby.StatusCode.NO_CONTENT);
      var c = this.factory.apply(ctx);
      c.logout();
      return ctx.getResponseCode();
    }

    public boolean supports(Class<AuthController> type) {
        return type == AuthController.class;
    }

    public io.jooby.Extension create(java.util.function.Supplier<AuthController> provider) {
        return new AuthController_(provider);
    }
}
