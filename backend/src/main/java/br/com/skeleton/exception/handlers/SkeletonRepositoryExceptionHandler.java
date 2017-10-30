package br.com.skeleton.exception.handlers;

import br.com.skeleton.exception.throwables.SkeletonException;
import br.com.skeleton.exception.throwables.repository.SkeletonRepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@ControllerAdvice
@RestController
public class SkeletonRepositoryExceptionHandler {

    private ErrorMessageBuilder errorMessageBuilder;

    @Autowired
    public SkeletonRepositoryExceptionHandler(final ErrorMessageBuilder errorMessageBuilder) {
        this.errorMessageBuilder = errorMessageBuilder;
    }

    @ExceptionHandler(value = SkeletonException.class)
    public ResponseEntity skeletonException(final SkeletonRepositoryException ex) {
        return errorMessageBuilder.build(ex).toResponseEntity();
    }

}
