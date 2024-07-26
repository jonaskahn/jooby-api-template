package io.github.jonaskahn.exception;

import lombok.Getter;

@Getter
public class LogicException extends RuntimeException {

    private Object[] variables;

    public LogicException(String message) {
        super(message);
    }

    public LogicException(Throwable cause) {
        super(cause);
    }

    public LogicException(Throwable cause, String message) {
        super(message, cause);
    }

    public LogicException(Throwable cause, String message, Object... args) {
        super(message, cause);
        this.variables = args;
    }
}
