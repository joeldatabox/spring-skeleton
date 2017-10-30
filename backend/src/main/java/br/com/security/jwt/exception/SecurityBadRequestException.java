package br.com.security.jwt.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SecurityBadRequestException extends SecurityUnauthorizedException {

    public SecurityBadRequestException(final Class clazz, final String field, final String message) {
        super("Bad Request", HttpStatus.BAD_REQUEST, clazz, field, message);
    }
}
