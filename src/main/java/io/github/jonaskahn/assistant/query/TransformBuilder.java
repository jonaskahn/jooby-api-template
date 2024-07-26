package io.github.jonaskahn.assistant.query;

public interface TransformBuilder<T> {
    QueryExecutor<T> build();
}
