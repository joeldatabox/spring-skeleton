package br.com.skeleton.repository;

import br.com.skeleton.model.Banco;
import org.springframework.stereotype.Repository;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Repository
public interface BancoRepository extends SkeletonRepositoryCustom<Banco, String> {
    Banco findByCodigo(final String string);
}

