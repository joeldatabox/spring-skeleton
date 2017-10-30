package br.com.security.repository;

import br.com.security.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}

