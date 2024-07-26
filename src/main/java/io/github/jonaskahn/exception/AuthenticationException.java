package io.github.jonaskahn.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("Authentication Exception");
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
