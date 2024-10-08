package io.github.jonaskahn;

import io.github.jonaskahn.assistant.JacksonMapper;
import io.github.jonaskahn.assistant.Language;
import io.github.jonaskahn.assistant.Response;
import io.github.jonaskahn.controllers.HealthController;
import io.github.jonaskahn.controllers.TestRoleController;
import io.github.jonaskahn.controllers.auth.AuthController;
import io.github.jonaskahn.controllers.user.UserController;
import io.github.jonaskahn.exception.*;
import io.github.jonaskahn.extensions.RedisModule;
import io.github.jonaskahn.middlewares.context.LanguageContextHolder;
import io.github.jonaskahn.middlewares.jwt.AdvancedJwtAuthenticator;
import io.jooby.*;
import io.jooby.exception.NotFoundException;
import io.jooby.exception.UnauthorizedException;
import io.jooby.flyway.FlywayModule;
import io.jooby.guice.GuiceModule;
import io.jooby.handler.Cors;
import io.jooby.handler.CorsHandler;
import io.jooby.hibernate.HibernateModule;
import io.jooby.hibernate.TransactionalRequest;
import io.jooby.hikari.HikariModule;
import io.jooby.jackson.JacksonModule;
import io.jooby.netty.NettyServer;
import io.jooby.pac4j.Pac4jModule;
import jakarta.persistence.NoResultException;
import org.pac4j.core.authorization.authorizer.CheckHttpMethodAuthorizer;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.http.client.direct.HeaderClient;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import redis.clients.jedis.JedisPooled;

import java.time.Duration;

public class App extends Jooby {
    {
        install(new NettyServer());
        install(new OpenAPIModule());
        install(new JacksonModule(JacksonMapper.INSTANCE));

        install(new GuiceModule());

        install(new RedisModule());

        install(new HikariModule());
        install(new FlywayModule());
        install(new HibernateModule().scan("io.github.jonaskahn.entities"));

        var corsOpts = new Cors();
        corsOpts.allowHeader("*");
        corsOpts.allowMethod("*");
        corsOpts.allowOrigin("*");
        corsOpts.setUseCredentials(true);
        corsOpts.setMaxAge(Duration.ofMinutes(60));
        use(new CorsHandler());

        install(new Pac4jModule().client(
                        "/api/secure/*",
                        new CheckHttpMethodAuthorizer(
                                HttpConstants.HTTP_METHOD.GET,
                                HttpConstants.HTTP_METHOD.PUT,
                                HttpConstants.HTTP_METHOD.DELETE,
                                HttpConstants.HTTP_METHOD.PATCH
                        ),
                        conf -> new HeaderClient(
                                "Authorization",
                                "Bearer ",
                                new AdvancedJwtAuthenticator(
                                        require(JedisPooled.class),
                                        new SecretSignatureConfiguration(conf.getString("jwt.salt")
                                        )
                                )
                        )
                )
        );


        use(new TransactionalRequest().enabledByDefault(true));
        setContextAsService(true);

        before((ctx) -> {
            var language = ctx.header("Accept-Language").valueOrNull();
            LanguageContextHolder.set(language);
        });

        after((ctx, result, failure) -> {
            if (failure == null) {
                ctx.setDefaultResponseType(MediaType.json);
                handleSuccess(ctx, result);
            }
        });

        error((ctx, cause, code) -> {
            ctx.setDefaultResponseType(MediaType.json);
            handleFailure(ctx, cause);
        });

        assets("/*", "static");

        mount("/api", new Jooby() {
            {
                install(new JacksonModule(JacksonMapper.INSTANCE));
                mvc(UserController.class);
                mvc(AuthController.class);
                mvc(HealthController.class);
                mvc(TestRoleController.class);
            }
        });
    }

    private static void handleSuccess(Context ctx, Object result) {
        ctx.setResponseCode(StatusCode.OK);
        if (result instanceof Response<?>) {
            ctx.render(result);
        } else if (result instanceof StatusCode) {
            ctx.render(Response.ok());
        } else {
            ctx.render(Response.ok(result));
        }
    }

    private static void handleFailure(Context ctx, Throwable failure) {
        if (failure instanceof LogicException ex) {
            ctx.setResponseCode(StatusCode.BAD_REQUEST_CODE);
            ctx.render(Response.fail(Language.of(ex.getMessage(), ex.getVariables()), StatusCode.BAD_REQUEST));
        } else if (failure instanceof ValidationException ex) {
            ctx.setResponseCode(StatusCode.PRECONDITION_FAILED);
            ctx.render(Response.fail(ex.getData(), StatusCode.PRECONDITION_FAILED));
        } else if (failure instanceof NotFoundException ex) {
            ctx.setResponseCode(StatusCode.NOT_FOUND);
            ctx.render(Response.fail(Language.of("app.common.exception.notfound"), StatusCode.NOT_FOUND));
        } else if (failure instanceof AuthenticationException ex) {
            ctx.setResponseCode(StatusCode.BAD_REQUEST);
            ctx.render(Response.fail(Language.of(ex.getMessage()), StatusCode.BAD_REQUEST));
        } else if (failure instanceof AuthorizationException) {
            ctx.setResponseCode(StatusCode.UNAUTHORIZED);
            ctx.render(Response.fail(Language.of("app.common.exception.AuthorizationException"), StatusCode.UNAUTHORIZED));
        } else if (failure instanceof UnauthorizedException || failure instanceof ForbiddenAccessException) {
            ctx.setResponseCode(StatusCode.UNAUTHORIZED);
            ctx.render(Response.fail(Language.of("app.common.exception.AuthorizationException"), StatusCode.UNAUTHORIZED));
        } else if (failure instanceof NoResultException) {
            ctx.setResponseCode(StatusCode.BAD_REQUEST);
            ctx.render(Response.fail(Language.of("app.common.exception.no-data"), StatusCode.BAD_REQUEST));
        } else if (failure instanceof Exception) {
            ctx.setResponseCode(StatusCode.SERVER_ERROR);
            ctx.render(Response.fail(Language.of("app.common.exception.server-error"), StatusCode.SERVER_ERROR));
        } else {
            ctx.setResponseCode(StatusCode.SERVER_ERROR);
            ctx.render(Response.fail(Language.of("app.common.exception.unknown-error"), StatusCode.SERVER_ERROR));
        }
    }

    public static void main(final String[] args) {
        runApp(args, App::new);
    }
}
