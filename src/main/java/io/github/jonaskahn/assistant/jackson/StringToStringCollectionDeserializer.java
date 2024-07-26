package io.github.jonaskahn.assistant.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class StringToStringCollectionDeserializer extends JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        var value = Optional.ofNullable(p.getValueAsString())
                .orElse(StringUtils.EMPTY);
        return StreamEx.of(value.split(","))
                .map(it -> it.replace("[", "").replace("]", ""))
                .map(String::trim)
                .map(it -> it.substring(1))
                .map(String::trim)
                .map(StringUtils::chop)
                .toList();
    }
}
