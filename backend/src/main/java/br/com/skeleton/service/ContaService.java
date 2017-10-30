package br.com.skeleton.service;

import br.com.skeleton.model.Conta;
import br.com.security.model.User;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Validated
public interface ContaService extends SkeletonService<Conta, String> {

    Conta getContaPrincipal(final User user);

    Conta findByNome(final User user, final String nome);

    List<Conta> findAll(final User user);
}
