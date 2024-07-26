package io.github.jonaskahn.controllers.user;

import io.github.jonaskahn.services.user.UserDto;
import io.github.jonaskahn.services.user.UserService;
import io.jooby.annotation.GET;
import io.jooby.annotation.POST;
import io.jooby.annotation.Path;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Path("")
public class UserController {
    private final UserService userService;

    @GET("/secure/user/info")
    public UserDto info() {
        return userService.getCurrentUserInfo();
    }

    @GET("/secure/user/info-with-executor")
    public UserDto getInfo() {
        return userService.getCurrentUserInfoWithExecutor();
    }

    @POST("/user/register")
    public void register(UserRegisterRequest request) {
        userService.createUser(request);
    }
}
