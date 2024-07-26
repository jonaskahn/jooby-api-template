package io.github.jonaskahn.controllers.user;

@io.jooby.annotation.Generated(UserController.class)
public class UserController_ implements io.jooby.MvcExtension, io.jooby.MvcFactory<UserController> {
    protected final java.util.function.Function<io.jooby.Context, UserController> factory;

    public UserController_() {
      this(UserController.class);
    }

    public UserController_(Class<UserController> type) {
      this(ctx -> ctx.require(type));
    }

    public UserController_(UserController instance) {
        this(ctx -> instance);
    }

    public UserController_(java.util.function.Supplier<UserController> provider) {
        this(ctx -> provider.get());
    }

    public UserController_(java.util.function.Function<io.jooby.Context, UserController> factory) {
        this.factory = factory;
    }

    public void install(io.jooby.Jooby app) throws Exception {
      /** See {@link UserController#info() */
      app.get("/secure/user/info", this::info)
        .setReturnType(io.github.jonaskahn.services.user.UserDto.class)
        .setMvcMethod(UserController.class.getMethod("info"));

      /** See {@link UserController#getInfo() */
      app.get("/secure/user/info-with-executor", this::getInfo)
        .setReturnType(io.github.jonaskahn.services.user.UserDto.class)
        .setMvcMethod(UserController.class.getMethod("getInfo"));

      /** See {@link UserController#register(io.github.jonaskahn.controllers.user.UserRegisterRequest) */
      app.post("/user/register", this::register)
        .setReturnType(io.jooby.StatusCode.class)
        .setMvcMethod(UserController.class.getMethod("register", io.github.jonaskahn.controllers.user.UserRegisterRequest.class));
    }

    public io.github.jonaskahn.services.user.UserDto info(io.jooby.Context ctx) {
      var c = this.factory.apply(ctx);
      return c.info();
    }

    public io.github.jonaskahn.services.user.UserDto getInfo(io.jooby.Context ctx) {
      var c = this.factory.apply(ctx);
      return c.getInfo();
    }

    public io.jooby.StatusCode register(io.jooby.Context ctx) {
      ctx.setResponseCode(io.jooby.StatusCode.OK);
      var c = this.factory.apply(ctx);
      c.register(ctx.body(io.github.jonaskahn.controllers.user.UserRegisterRequest.class));
      return ctx.getResponseCode();
    }

    public boolean supports(Class<UserController> type) {
        return type == UserController.class;
    }

    public io.jooby.Extension create(java.util.function.Supplier<UserController> provider) {
        return new UserController_(provider);
    }
}
