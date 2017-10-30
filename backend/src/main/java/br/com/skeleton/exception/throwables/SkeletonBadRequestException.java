package br.com.skeleton.exception.throwables;

import org.springframework.http.HttpStatus;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SkeletonBadRequestException extends SkeletonException {

    public SkeletonBadRequestException(final Class clazz, final String field, final String message) {
        super("Bad Request", HttpStatus.BAD_REQUEST, clazz, field, message);
    }
}
