package io.github.jonaskahn.services.user;

import io.github.jonaskahn.exception.LogicException;

public class UserExistException extends LogicException {
    public UserExistException() {
        super("app.users.exception.user-existed");
    }
}
