package br.com.skeleton.util;

import br.com.skeleton.exception.throwables.SkeletonException;
import com.fasterxml.jackson.core.JsonStreamContext;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Component
public class ValidatorUtil {
    private final ValidatorFactory factory;

    public ValidatorUtil() {
        this.factory = Validation.buildDefaultValidatorFactory();
    }

    /**
     * Verifica se existe alguma Annotation do tipo @NotNull, @NotBlank,
     *
     * @Size e aplica a validação!
     */
    public void runValidation(final JsonStreamContext context) throws SkeletonException {

    }

    public <T> void validate(final T instance) {
        final Validator validator = factory.getValidator();
        final Set<ConstraintViolation<T>> violations = validator.validate(instance, Default.class);
        if (!violations.isEmpty()) {
            final Set<ConstraintViolation<?>> constraints = new HashSet<ConstraintViolation<?>>(violations.size());
            for (final ConstraintViolation<?> violation : violations) {
                constraints.add(violation);
            }
            throw new ConstraintViolationException(constraints);
        }

    }
}
