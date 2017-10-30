package br.com.security.jwt.exception;

import br.com.security.model.Authority;
import br.com.security.model.User;
import br.com.security.model.enumeration.Authorities;
import org.springframework.http.HttpStatus;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SecurityCredentialException extends SecurityUnauthorizedException {

    public SecurityCredentialException(final String message, Authority authority) {
        this(message, authority.getName());
    }

    public SecurityCredentialException(final String message, Authorities authority) {
        super(message, HttpStatus.FORBIDDEN, User.class, "authorities", (authority != null ? authority.name() : ""));
    }
}
