package br.com.skeleton.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Component
public class Validator {

    private final javax.validation.Validator validator;

    @Autowired
    public Validator(final javax.validation.Validator validator) {
        this.validator = validator;
    }

    public final void validate(final Object o) {
        final Set<ConstraintViolation<Object>> violations = validator.validate(o);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException("test", violations);
        }
    }
}
