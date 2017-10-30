package br.com.security.jwt.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SecurityNotFoundException extends SecurityUnauthorizedException {

    public SecurityNotFoundException(final Throwable cause) {
        super(cause);
    }

    public SecurityNotFoundException(final Class clazz, final String field, final String message) {
        super("AbstractResource Not Found", HttpStatus.NOT_FOUND, clazz, field, message);
    }
}
