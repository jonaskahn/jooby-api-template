package io.github.jonaskahn.middlewares.validate;

import io.github.jonaskahn.assistant.Language;
import io.github.jonaskahn.exception.ValidationException;
import io.jooby.Context;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PUBLIC, onConstructor_ = @__({@Inject}))
public class BeanValidatorImpl implements BeanValidator {
    private Context context;
    private Validator validator;

    @Override
    public void validate(Object obj) {
        var result = validator.validate(obj);
        if (result.isEmpty()) return;
        var acceptLanguage = context.header("Accept-Language").valueOrNull();
        var data = result.stream()
                .map(it -> Language.of(acceptLanguage, it.getMessage()))
                .filter(Objects::nonNull)
                .toList();
        throw new ValidationException(data);
    }
}
