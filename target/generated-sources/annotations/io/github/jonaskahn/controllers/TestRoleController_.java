package io.github.jonaskahn.controllers;

@io.jooby.annotation.Generated(TestRoleController.class)
public class TestRoleController_ implements io.jooby.MvcExtension, io.jooby.MvcFactory<TestRoleController> {
    protected final java.util.function.Function<io.jooby.Context, TestRoleController> factory;

    public TestRoleController_() {
      this(TestRoleController.class);
    }

    public TestRoleController_(Class<TestRoleController> type) {
      this(ctx -> ctx.require(type));
    }

    public TestRoleController_(TestRoleController instance) {
        this(ctx -> instance);
    }

    public TestRoleController_(java.util.function.Supplier<TestRoleController> provider) {
        this(ctx -> provider.get());
    }

    public TestRoleController_(java.util.function.Function<io.jooby.Context, TestRoleController> factory) {
        this.factory = factory;
    }

    public void install(io.jooby.Jooby app) throws Exception {
      /** See {@link TestRoleController#testAdmin() */
      app.get("/secure/test/admin-role", this::testAdmin)
        .setReturnType(String.class)
        .setMvcMethod(TestRoleController.class.getMethod("testAdmin"));
    }

    public String testAdmin(io.jooby.Context ctx) {
      var c = this.factory.apply(ctx);
      return c.testAdmin();
    }

    public boolean supports(Class<TestRoleController> type) {
        return type == TestRoleController.class;
    }

    public io.jooby.Extension create(java.util.function.Supplier<TestRoleController> provider) {
        return new TestRoleController_(provider);
    }
}
