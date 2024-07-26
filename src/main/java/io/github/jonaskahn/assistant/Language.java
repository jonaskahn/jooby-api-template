package io.github.jonaskahn.assistant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Language {

    private static final Logger log = LoggerFactory.getLogger(Language.class);

    private static final ResourceBundle en = ResourceBundle.getBundle("messages", Locale.ENGLISH);
    private static final ResourceBundle vi = ResourceBundle.getBundle("messages", Locale.of("vi"));

    private static final Map<String, ResourceBundle> languages = Map.of(
            "vi", vi,
            "vi-VN", vi,
            "en", en,
            "en-US", en
    );

    public static String of(String language, String key, Object... variables) {
        try {
            if (key == null || key.isEmpty()) return null;
            var bundle = Optional.ofNullable(languages.get(language));
            return bundle.map(b -> b.getString(key))
                    .map(it -> MessageFormat.format(it, variables))
                    .orElse(key);
        } catch (Exception e) {
            log.warn("Failed to parse message for key {} in language {}", key, language);
            return key;
        }
    }
}
