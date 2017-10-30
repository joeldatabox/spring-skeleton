package br.com.skeleton.exception.throwables;

import org.springframework.http.HttpStatus;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SkeletonMethodNotAllowedException extends SkeletonException {


    public SkeletonMethodNotAllowedException(final String message) {
        super(message, HttpStatus.METHOD_NOT_ALLOWED);
    }

    public SkeletonMethodNotAllowedException(final Class clazz, final String field, final String message) {
        super("Method not allowed", HttpStatus.METHOD_NOT_ALLOWED, clazz, field, message);
    }
}
