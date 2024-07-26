package io.github.jonaskahn.assistant.query;

import com.fasterxml.jackson.databind.JavaType;
import io.github.jonaskahn.assistant.JacksonMapper;
import jakarta.persistence.Query;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.NonUniqueResultException;
import org.hibernate.query.NativeQuery;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class JpaQueryExecutor<T> implements QueryExecutor<T> {
    private NativeQuery<Object> query;
    private Class<T> targetClass;

    public JpaQueryExecutor(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    public static <T> ExecutorBuilder<T> builder(Class<T> clazz) {
        return new ExecutorBuilder<>(clazz);
    }

    private static JavaType getCollectionType(Class collectionClass, Class... elementClasses) {
        return JacksonMapper.INSTANCE.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    @Override
    public Long count() {
        var result = getDatabaseResultAsList();
        if (result.isEmpty()) {
            return 0L;
        }
        if (result.size() > 1) {
            throw new NonUniqueResultException(result.size());
        }
        return (Long) result.get(0).values().iterator().next();
    }

    private List<HashMap<String, Object>> getDatabaseResultAsList() {
        return query.setTupleTransformer((tuple, aliases) -> {
            var data = new HashMap<String, Object>();
            for (int i = 0; i < aliases.length; i++) {
                data.put(aliases[i], tuple[i]);
            }
            return data;
        }).list();
    }

    @Override
    public List<T> list() {
        var result = getDatabaseResultAsList();
        return JacksonMapper.INSTANCE.convertValue(result, getCollectionType(ArrayList.class, targetClass));
    }

    @Override
    public T unique() {
        var result = list();
        var size = result.size();
        if (size == 0) return null;
        var first = result.get(0);
        for (int i = 1; i < size; i++) {
            if (Objects.equals(result.get(i), first)) {
                throw new NonUniqueResultException(result.size());
            }
        }
        return first;
    }

    @Override
    public T first() {
        var result = list();
        if (result.isEmpty()) return null;
        return result.get(0);
    }

    public static class ExecutorBuilder<R> implements QueryBuilder<R> {

        private final JpaQueryExecutor<R> executor;

        public ExecutorBuilder(Class<R> targetClass) {
            this.executor = new JpaQueryExecutor<>(targetClass);
        }

        @Override
        public QueryExecutor<R> with(Query query, Map<String, Object> params) {
            params.forEach(query::setParameter);
            executor.query = query.unwrap(NativeQuery.class);
            return executor;
        }

        @Override
        public QueryExecutor<R> with(Query query) {
            executor.query = query.unwrap(NativeQuery.class);
            return executor;
        }
    }
}
