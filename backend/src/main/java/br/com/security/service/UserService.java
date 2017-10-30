package br.com.security.service;

import br.com.security.jwt.JwtUser;
import br.com.security.model.Passwd;
import br.com.security.model.User;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public interface UserService {
    User save(final User user);

    boolean remove(final User user);

    User update(final User user);

    User updatePasswd(final Passwd user);

    User findByEmail(final String email);

    User findById(final String id);

    Collection<User> findAll();

    Authentication getCurrentAuthentication();

    JwtUser getCurrentJwtUser();

    User getCurrentUser();
}
