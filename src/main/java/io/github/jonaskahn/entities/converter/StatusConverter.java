package io.github.jonaskahn.entities.converter;

import io.github.jonaskahn.entities.enums.Status;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Optional;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Status status) {
        return Optional.ofNullable(status).map(Status::getId).orElse(null);
    }

    @Override
    public Status convertToEntityAttribute(Integer id) {
        return Status.of(id);
    }
}
