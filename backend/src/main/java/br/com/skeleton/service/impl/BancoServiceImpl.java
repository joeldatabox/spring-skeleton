package br.com.skeleton.service.impl;

import br.com.skeleton.exception.throwables.SkeletonConflictException;
import br.com.skeleton.exception.throwables.SkeletonNotFoundException;
import br.com.skeleton.model.Banco;
import br.com.skeleton.repository.BancoRepository;
import br.com.skeleton.service.BancoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Service
public class BancoServiceImpl implements BancoService {
    private final BancoRepository repository;

    @Autowired
    public BancoServiceImpl(final BancoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Banco save(final Banco banco) {
        try {
            findByCodigo(banco.getCodigo());
            throw new SkeletonConflictException(Banco.class, "codigo", "já existe um banco com esse código " + banco.getCodigo());
        } catch (SkeletonNotFoundException ex) {
        }
        return this.repository.save(banco);
    }

    @Override
    public Banco update(final Banco banco) {
        final Banco other = findById(banco.getId());
        if (other.getCodigo() != banco.getCodigo()) {
            try {
                final Banco other1 = findByCodigo(banco.getCodigo());
                if (!other1.getId().equals(banco.getId())) {
                    throw new SkeletonConflictException(Banco.class, "codigo", "já existe um banco com esse código " + banco.getCodigo());
                }
            } catch (SkeletonNotFoundException ex) {
            }
        }
        return this.repository.save(banco);
    }

    @Override
    public Banco findById(final String id) {
        final Banco banco = this.repository.findOne(id);
        if (banco == null) {
            throw new SkeletonNotFoundException(Banco.class, "id", "registro não encontrado").addDetails("id", id);
        }
        return banco;
    }

    @Override
    public Banco findByCodigo(final String codigo) {
        final Banco banco = this.repository.findByCodigo(codigo);
        if (banco == null) {
            throw new SkeletonNotFoundException(Banco.class, "codigo", "registro não encontrado");
        }
        return banco;
    }

    @Override
    public Collection<Banco> findAll() {
        final Collection<Banco> bancos = this.repository.findAll();
        if (bancos == null || bancos.isEmpty()) {
            throw new SkeletonNotFoundException(Banco.class, "", "registros inexistentes");
        }
        return bancos;
    }
}
