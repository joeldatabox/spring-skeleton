package br.com.skeleton.exception.throwables;

import org.springframework.http.HttpStatus;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SkeletonNotFoundException extends SkeletonException {

    public SkeletonNotFoundException(final Throwable cause) {
        super(cause);
    }

    public SkeletonNotFoundException(final Class clazz, final String field, final String message) {
        super("AbstractResource Not Found", HttpStatus.NOT_FOUND, clazz, field, message);
    }
}
