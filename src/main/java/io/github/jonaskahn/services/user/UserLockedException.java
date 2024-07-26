package io.github.jonaskahn.services.user;

import io.github.jonaskahn.exception.LogicException;

public class UserLockedException extends LogicException {
    public UserLockedException() {
        super("app.users.exception.user-locked");
    }
}
