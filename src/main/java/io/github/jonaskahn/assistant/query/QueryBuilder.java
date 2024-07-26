package io.github.jonaskahn.assistant.query;

import jakarta.persistence.Query;

import java.util.Map;

public interface QueryBuilder<T> {
    QueryExecutor<T> with(Query query, Map<String, Object> params);

    QueryExecutor<T> with(Query query);
}
