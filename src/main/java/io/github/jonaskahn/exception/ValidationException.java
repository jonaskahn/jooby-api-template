package io.github.jonaskahn.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private List<String> data;

    public ValidationException() {
        super("Submitted data is invalid");
    }

    public ValidationException(List<String> data) {
        super("Submitted data is invalid");
        this.data = data;
    }

}
