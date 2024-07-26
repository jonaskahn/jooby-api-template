package io.github.jonaskahn.middlewares.validate;

import com.google.inject.ImplementedBy;

@ImplementedBy(BeanValidatorImpl.class)
public interface BeanValidator {
    void validate(Object obj);
}
