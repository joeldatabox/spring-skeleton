package br.com.security.service.impl;

import br.com.security.jwt.JwtUser;
import br.com.security.jwt.exception.SecurityBadRequestException;
import br.com.security.jwt.exception.SecurityConflictException;
import br.com.security.jwt.exception.SecurityNotFoundException;
import br.com.security.model.Passwd;
import br.com.security.model.User;
import br.com.security.repository.UserRepository;
import br.com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImplementation(final UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(final User user) {
        return merge(user);
    }

    @Override
    public boolean remove(final User user) {
        final User other = findById(user.getId());
        repository.delete(other);
        return true;
    }

    @Override
    public User update(final User user) {
        return merge(user);
    }

    @Override
    public User updatePasswd(final Passwd passwd) {
        final User user = findById(passwd.getId());
        user.setPasswd(passwd);
        return save(user);
    }

    @Override
    public User findByEmail(final String email) {
        final User user = repository.findByEmail(email);
        if (user == null) {
            throw new SecurityNotFoundException(User.class, "email", email + " este registro não foi encontrado");
        }
        return user;
    }

    @Override
    public User findById(final String id) {
        final User user = repository.findOne(id);
        if (user == null) {
            throw new SecurityNotFoundException(User.class, "id", id + " este registro não foi encontrado");
        }
        return user;
    }

    @Override
    public Collection<User> findAll() {
        return (Collection<User>) repository.findAll();
    }

    @Override
    public Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public JwtUser getCurrentJwtUser() {
        return (JwtUser) getCurrentAuthentication().getPrincipal();
    }

    @Override
    public User getCurrentUser() {
        return getCurrentJwtUser().getOriginUser();
    }

    private User merge(final User user) {
        if (user.getNome() == null || user.getNome().length() < 4) {
            throw new SecurityBadRequestException(User.class, "nome", "O campo nome deve ter de 4 a 200 caracteres!");
        }
        if (!user.isValidEmail()) {
            throw new SecurityBadRequestException(User.class, "email", "Informe um email válido!");
        }
        if (user.getId() == null) {
            //validando se já existe esse usuário no sistema
            try {
                if (findByEmail(user.getEmail()) != null) {
                    throw new SecurityConflictException(User.class, "email", "Email já cadastrado!");
                }
            } catch (SecurityNotFoundException ex) {
            }
            //validando se preencheu a senha corretamente
            if (!user.isValidPasswd()) {
                throw new SecurityBadRequestException(User.class, "passwd", "Informe uma senha válida!");
            }
            return repository.save(user);
        } else {
            final User self = findById(user.getId());

            if (!self.getEmail().equals(user.getEmail())) {
                throw new SecurityBadRequestException(User.class, "email", "O email não pode ser modificado!").setStatus(HttpStatus.NOT_ACCEPTABLE);
            }

            //garantindo que a senha não irá ser modificada
            user.setPasswd(self);

            return repository.save(user);
        }
    }


    public Long count(final User user, final Map<String, Object> allRequestParams) {
        //return repository.count(allRequestParams);
        throw new UnsupportedOperationException("Implemente o methodo");
    }


    public List<User> findAll(final User user, final Map<String, Object> allRequestParams) {
        throw new UnsupportedOperationException("Implemente o methodo");
        //return repository.findAll(allRequestParams);
    }
}
