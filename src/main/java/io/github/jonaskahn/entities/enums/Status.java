package io.github.jonaskahn.entities.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import one.util.streamex.StreamEx;

import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Status {
    INACTIVATED(Code.INACTIVATED, "app.common.enum.status.inactive"),
    ACTIVATED(Code.ACTIVATED, "app.common.enum.status.active"),
    LOCK(Code.LOCK, "app.common.enum.status.lock"),
    DELETED(Code.DELETED, "app.common.enum.status.deleted");

    private static final Map<Integer, Status> statuses = StreamEx.of(Status.values()).toMap(Status::getId, Function.identity());

    private final Integer id;
    private final String description;

    public static Status of(final Integer id) {
        return statuses.get(id);
    }

    @JsonValue
    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Code {
        public static final Integer INACTIVATED = 0;
        public static final Integer ACTIVATED = 1;
        public static final Integer LOCK = 2;
        public static final Integer DELETED = 9;
    }
}
