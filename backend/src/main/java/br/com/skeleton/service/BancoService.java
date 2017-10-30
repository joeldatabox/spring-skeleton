package br.com.skeleton.service;

import br.com.skeleton.model.Banco;

import java.util.Collection;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public interface BancoService {
    Banco save(final Banco banco);

    Banco update(final Banco banco);

    Banco findById(final String id);

    Banco findByCodigo(String codiog);

    Collection<Banco> findAll();

}
