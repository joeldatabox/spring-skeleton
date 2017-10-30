package br.com.skeleton.exception.throwables;

import org.springframework.http.HttpStatus;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SkeletonNotAcceptableException extends SkeletonException {

    public SkeletonNotAcceptableException(final Class clazz, final String field, final String message) {
        super("Not Acceptable", HttpStatus.NOT_ACCEPTABLE, clazz, field, message);
    }
}
