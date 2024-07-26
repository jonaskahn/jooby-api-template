package io.github.jonaskahn.exception;

public class ShouldNeverOccurException extends RuntimeException {
    public ShouldNeverOccurException() {
        super("Should never occur");
    }
}
