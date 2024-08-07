package io.github.jonaskahn.assistant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.jooby.StatusCode;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"status", "timestamp", "payload"})
public class Response<T> {
    private Integer status;
    private String message;
    private T payload;

    public static <T> Response<T> ok() {
        return Response.<T>builder()
                .status(StatusCode.OK.value())
                .message(Language.of("app.common.message.success"))
                .build();
    }

    public static <T> Response<T> ok(T payload) {
        return Response.<T>builder()
                .status(StatusCode.OK.value())
                .message(Language.of("app.common.message.success"))
                .payload(payload)
                .build();
    }

    public static <T> Response<T> ok(String message, T payload) {
        return Response.<T>builder()
                .status(StatusCode.OK.value())
                .message(Language.of(message))
                .payload(payload)
                .build();
    }

    public static <T> Response<T> fail(T payload, @NonNull StatusCode code) {
        return Response.<T>builder()
                .status(code.value())
                .message(Language.of("app.common.message.failed"))
                .payload(payload)
                .build();
    }

    public static Response<String> fail(String message, @NonNull StatusCode code) {
        return Response.<String>builder()
                .status(code.value())
                .message(Language.of(message))
                .build();
    }

    public static <T> Response<T> fail(String message, T payload, @NonNull StatusCode code) {
        return Response.<T>builder()
                .status(code.value())
                .message(Language.of(message))
                .payload(payload)
                .build();
    }
}
