package io.github.jonaskahn.middlewares.validate;

import io.github.jonaskahn.assistant.Language;
import io.github.jonaskahn.exception.ValidationException;
import io.github.jonaskahn.middlewares.context.LanguageContextHolder;
import jakarta.validation.Validation;

import java.util.Objects;

public final class BeanValidator {
    public static void validate(Object obj) {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var result = factory.getValidator().validate(obj);
            if (result.isEmpty()) return;
            var acceptLanguage = LanguageContextHolder.currentLanguage();
            var data = result.stream()
                    .map(it -> Language.of(acceptLanguage, it.getMessage()))
                    .filter(Objects::nonNull)
                    .toList();
            throw new ValidationException(data);
        }

    }
}
