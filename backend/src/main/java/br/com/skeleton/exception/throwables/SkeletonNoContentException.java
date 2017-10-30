package br.com.skeleton.exception.throwables;

import org.springframework.http.HttpStatus;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SkeletonNoContentException extends SkeletonException {

    public SkeletonNoContentException(final Class clazz, final String field, final String message) {
        super("No Content", HttpStatus.NO_CONTENT, clazz, field, message);
    }
}
