package io.github.jonaskahn.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super("Unauthorized Access");
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
