package br.com.skeleton.exception.throwables;

import org.springframework.http.HttpStatus;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SkeletonPageableRequestException extends SkeletonException {

    public SkeletonPageableRequestException() {
        this("invalid params of pagination");
    }

    public SkeletonPageableRequestException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
