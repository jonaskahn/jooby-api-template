package io.github.jonaskahn.extensions;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.jooby.Extension;
import io.jooby.Jooby;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.SneakyThrows;

public class ValidatorModule implements Extension {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    @Override
    @SneakyThrows(Exception.class)
    public void install(@NonNull Jooby application) {
        var services = application.getServices();
        services.put(Validator.class, factory.getValidator());
        application.onStop(factory::close);
    }
}
