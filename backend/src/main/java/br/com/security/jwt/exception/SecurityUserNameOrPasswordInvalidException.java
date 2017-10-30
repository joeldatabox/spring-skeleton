package br.com.security.jwt.exception;

import org.springframework.http.HttpStatus;
/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SecurityUserNameOrPasswordInvalidException extends SecurityUnauthorizedException {

    public SecurityUserNameOrPasswordInvalidException() {
        this("Usuário e/ou senha incorreto(s)");
        this.status = HttpStatus.UNAUTHORIZED;
    }

    public SecurityUserNameOrPasswordInvalidException(final String message) {
        super(message);
        this.status = HttpStatus.UNAUTHORIZED;
    }
}
