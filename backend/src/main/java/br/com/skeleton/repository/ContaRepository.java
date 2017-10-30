package br.com.skeleton.repository;

import br.com.skeleton.model.Conta;
import br.com.security.model.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Repository
public interface ContaRepository extends SkeletonRepositoryCustom<Conta, String> {

    @Query("{'user': {'$ref' : 'users', '$id' : ?0}, 'nome': '?1' }")
    Conta findByNome(final String userId, final String nome);

    @Query("{'user': {'$ref' : 'users', '$id' : ?0}, 'principal': true }")
    Conta getContaPrincipal(final String userId);

    List<Conta> findByUser(User user);
}

