package io.github.jonaskahn.entities.converter;

import io.github.jonaskahn.assistant.DataUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter(autoApply = true)
public class StringCollectionConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> lst) {
        return DataUtils.convertJsonToString(lst);
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        return DataUtils.convertJsonStringCollection(s);
    }
}
