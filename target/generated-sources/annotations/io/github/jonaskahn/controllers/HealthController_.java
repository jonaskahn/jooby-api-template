package io.github.jonaskahn.controllers;

@io.jooby.annotation.Generated(HealthController.class)
public class HealthController_ implements io.jooby.MvcExtension, io.jooby.MvcFactory<HealthController> {
    protected final java.util.function.Function<io.jooby.Context, HealthController> factory;

    public HealthController_() {
      this(new HealthController());
    }

    public HealthController_(HealthController instance) {
        this(ctx -> instance);
    }

    public HealthController_(java.util.function.Supplier<HealthController> provider) {
        this(ctx -> provider.get());
    }

    public HealthController_(java.util.function.Function<io.jooby.Context, HealthController> factory) {
        this.factory = factory;
    }

    public void install(io.jooby.Jooby app) throws Exception {
      /** See {@link HealthController#up() */
      app.get("/health", this::up)
        .setAttributes(java.util.Map.of(
            "Operation.deprecated", false,
            "Operation.hidden", false,
            "Operation.ignoreJsonView", false,
            "Operation.requestBody", 
              java.util.Map.of(
                 "RequestBody.required", false,
                 "RequestBody.useParameterTypeSchema", false),
            "Operation.summary", "Health Check Endpoints"))
        .setReturnType(String.class)
        .setMvcMethod(HealthController.class.getMethod("up"));
    }

    public String up(io.jooby.Context ctx) {
      var c = this.factory.apply(ctx);
      return c.up();
    }

    public boolean supports(Class<HealthController> type) {
        return type == HealthController.class;
    }

    public io.jooby.Extension create(java.util.function.Supplier<HealthController> provider) {
        return new HealthController_(provider);
    }
}
