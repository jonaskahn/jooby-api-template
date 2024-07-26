package io.github.jonaskahn.services.user;

import com.google.inject.ImplementedBy;
import io.github.jonaskahn.controllers.user.UserRegisterRequest;

@ImplementedBy(UserServiceImpl.class)
public interface UserService {
    void createUser(UserRegisterRequest request);

    UserDto getCurrentUserInfo();

    UserDto getCurrentUserInfoWithExecutor();
}
