package io.github.jonaskahn.assistant;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataUtils {

    private static final Logger log = LoggerFactory.getLogger(DataUtils.class);

    public static List<String> convertJsonStringCollection(String data) {
        if (StringUtils.isEmpty(data)) {
            return List.of();
        }
        try {
            return JacksonMapper.INSTANCE.readValue(data, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.warn("Cannot convert json string to list", e);
            return List.of();
        }
    }

    public static <T> String convertJsonToString(T data) {
        if (ObjectUtils.isEmpty(data)) return null;
        try {
            return JacksonMapper.INSTANCE.writeValueAsString(data);
        } catch (Exception e) {
            log.warn("Cannot convert data to json string", e);
            return StringUtils.EMPTY;
        }
    }

}
