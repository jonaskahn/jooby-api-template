package io.github.jonaskahn.exception;

public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException() {
        super("Forbidden access");
    }

    public ForbiddenAccessException(String message) {
        super(message);
    }
}
