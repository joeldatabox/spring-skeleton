package br.com.skeleton.exception.handlers;

import br.com.security.jwt.exception.SecurityUnauthorizedException;
import br.com.security.jwt.exception.SecurityUserNameOrPasswordInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@ControllerAdvice
@RestController
public class SecurityExceptionHandler {

    private final ErrorMessageBuilder errorMessageBuilder;

    @Autowired
    public SecurityExceptionHandler(final ErrorMessageBuilder errorMessageBuilder) {
        this.errorMessageBuilder = errorMessageBuilder;
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity usernameNotFoundException(final UsernameNotFoundException ex) {
        SecurityUserNameOrPasswordInvalidException exx = new SecurityUserNameOrPasswordInvalidException();
        exx.addSuppressed(ex);
        return usernameNotFoundException(exx);
    }

    @ExceptionHandler(value = SecurityUserNameOrPasswordInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity usernameNotFoundException(final SecurityUserNameOrPasswordInvalidException ex) {
        return errorMessageBuilder.build(ex).toResponseEntity();
    }

    @ExceptionHandler(value = SecurityUnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity usernameNotFoundException(final SecurityUnauthorizedException ex) {
        return errorMessageBuilder.build(ex).toResponseEntity();
    }
}
