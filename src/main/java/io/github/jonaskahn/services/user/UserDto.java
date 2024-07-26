package io.github.jonaskahn.services.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.jonaskahn.assistant.jackson.StringToStringCollectionDeserializer;
import io.github.jonaskahn.entities.enums.Status;
import lombok.Builder;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Builder
public record UserDto(
        Long id,
        String username,
        String email,
        @JsonAlias("full_name")
        String fullName,
        Status status,
        @JsonDeserialize(using = StringToStringCollectionDeserializer.class)
        List<String> roles,
        @JsonAlias("created_at")
        Instant createdAt,
        @JsonAlias("created_by")
        Long createdBy,
        @JsonAlias("updated_at")
        Instant updatedAt,
        @JsonAlias("updated_by")
        Long updatedBy
) implements Serializable {

}