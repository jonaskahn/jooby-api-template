package io.github.jonaskahn.assistant.query;

import java.util.List;

public interface QueryExecutor<T> {
    Long count();

    List<T> list();

    T unique();

    T first();


    default List<T> getListResult() {
        return list();
    }

    default T getSingleResult() {
        return unique();
    }

    default T getFirstResult() {
        return first();
    }
}
