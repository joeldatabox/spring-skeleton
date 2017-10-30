package br.com.security.jwt.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SecurityConflictException extends SecurityUnauthorizedException {

    public SecurityConflictException(final Class clazz, final String field, final String message) {
        super("Conflict", HttpStatus.CONFLICT, clazz, field, message);
        /*this.message = "Conflict";
        this.status = HttpStatus.CONFLICT;
        this.objectName = clazz.getSimpleName().toLowerCase();
        this.details.put(this.objectName + "." + field, message);*/
    }
}
