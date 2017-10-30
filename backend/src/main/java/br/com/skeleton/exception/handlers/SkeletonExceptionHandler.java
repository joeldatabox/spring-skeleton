package br.com.skeleton.exception.handlers;

import br.com.skeleton.exception.throwables.SkeletonException;
import br.com.skeleton.exception.throwables.SkeletonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@ControllerAdvice
@RestController
public class SkeletonExceptionHandler {

    private final ErrorMessageBuilder errorMessageBuilder;

    @Autowired
    public SkeletonExceptionHandler(final ErrorMessageBuilder errorMessageBuilder) {
        this.errorMessageBuilder = errorMessageBuilder;
    }

    @ExceptionHandler(value = SkeletonException.class)
    public ResponseEntity skeletonException(final SkeletonException ex) {
        return errorMessageBuilder.build(ex).toResponseEntity();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity not(final ResourceNotFoundException ex) {
        return errorMessageBuilder.build(new SkeletonNotFoundException(ex)).toResponseEntity();
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity not(final NoHandlerFoundException ex) {
        return errorMessageBuilder.build(new SkeletonNotFoundException(ex)).toResponseEntity();
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity validationException(final ConstraintViolationException ex) {
        return errorMessageBuilder.build(ex).toResponseEntity();
    }


}
