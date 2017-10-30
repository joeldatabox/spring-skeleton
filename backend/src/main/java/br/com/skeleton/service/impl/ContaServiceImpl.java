package br.com.skeleton.service.impl;

import br.com.security.model.User;
import br.com.skeleton.exception.throwables.SkeletonBadRequestException;
import br.com.skeleton.exception.throwables.SkeletonConflictException;
import br.com.skeleton.exception.throwables.SkeletonNotFoundException;
import br.com.skeleton.model.Conta;
import br.com.skeleton.repository.ContaRepository;
import br.com.skeleton.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Service
public class ContaServiceImpl extends SkeletonServiceImpl<Conta, String> implements ContaService {


    private final ContaRepository repository;

    @Autowired
    public ContaServiceImpl(final ContaRepository repository) {
        super(repository, Conta.class);
        this.repository = repository;
    }

    @Override
    public Conta getContaPrincipal(final User user) {
        final Conta conta = this.repository.getContaPrincipal(user.getId());
        if (conta == null) {
            throw new SkeletonNotFoundException(Conta.class, "principal", "Não existe uma conta principal").addDetails("principal", true);
        }
        return conta;
    }

    @Override
    public Conta findByNome(final User user, final String nome) {
        final Conta conta = repository.findByNome(user.getId(), nome);
        if (conta == null) {
            throw new SkeletonNotFoundException(Conta.class, "descricao", "nenhuma conta encontrada com essa descrição");
        }
        return conta;
    }

    @Override
    public List<Conta> findAll(final User user) {
        return super.findAll(user, null);
    }

    @Override
    public void beforeDelete(final User user, final String value) {
        //Caso a conta seja principal devemos marcar alguma outra como principal
        try {
            this.getContaPrincipal(user);
        } catch (final SkeletonNotFoundException ex) {
            //se não encontrou vamos pegar uma qualquer e marcar como principal
            final Conta conta = this.repository.findFirst(user);
            if (conta != null) {
                conta.setPrincipal(true);
                this.update(user, conta);
            }
        }
        super.beforeDelete(user, value);
    }

    @Override
    public void beforeDelete(final User user, final Conta value) {
        this.beforeDelete(user, value.getId());
        super.beforeDelete(user, value);
    }

    @Override
    public void checkPrecondictionSave(final User user, final Conta conta) {
        //verificando se já existe uma conta com o mesmo nome
        try {
            findByNome(user, conta.getNome());
            throw new SkeletonConflictException(Conta.class, "descricao", "já existe um registro com essa descrição \"" + conta.getNome() + "\"");
        } catch (final SkeletonNotFoundException ex) {
        }
        //validação de conta principal
        checkContaPrincipal(user, conta);
        super.checkPrecondictionSave(user, conta);
    }

    @Override
    public void checkPrecondictionUpdate(final User user, final Conta conta) {
        //verificando se o nome alterado já existe
        try {
            //se o id ta diferente entao quer dizer que tem um nome igual o que no caso não é permitidos
            if (!findByNome(user, conta.getNome()).equals(conta))
                throw new SkeletonConflictException(Conta.class, "descricao", "já existe um registro com essa descrição \"" + conta.getNome() + "\"");
        } catch (final SkeletonNotFoundException ex) {
        }
        //validação de conta principal
        checkContaPrincipal(user, conta);
        super.checkPrecondictionUpdate(user, conta);
    }

    /**
     * Devemos garatir que tenha apenas uma conta marcada como principal
     *
     * @param user  -> usuário atual
     * @param conta -> conta a ser validada
     */
    private void checkContaPrincipal(final User user, final Conta conta) {
        try {
            final Conta other = getContaPrincipal(user);
            //se a conta for diferente e a passada por parametro for "principal" devemos alterar a mesma para não principal
            if (!other.equals(conta) && conta.isPrincipal()) {
                other.setPrincipal(false);
                this.repository.save(user, other);
            }
            //se tentar transformar a unica conta principal como não principal devemos lançar uma exceção
            if (other.equals(conta) && !conta.isPrincipal()) {
                throw new SkeletonBadRequestException(Conta.class, "principal", "Você deve deixar ao menos uma conta marcada como principal")
                        .addDetails("principal", conta.isPrincipal());
            }
        } catch (final SkeletonNotFoundException ex) {
            //se não encontrou nada então devemos garantir que essa conta será a principal
            conta.setPrincipal(true);
        }
    }
}