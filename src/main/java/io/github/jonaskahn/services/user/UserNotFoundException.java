package io.github.jonaskahn.services.user;

import io.github.jonaskahn.exception.LogicException;

public class UserNotFoundException extends LogicException {
    public UserNotFoundException() {
        super("app.users.exception.user-not-found");
    }
}
