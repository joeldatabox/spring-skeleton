package br.com.skeleton.service.impl;

import br.com.skeleton.config.Validator;
import br.com.skeleton.exception.throwables.SkeletonBadRequestException;
import br.com.skeleton.exception.throwables.SkeletonNoContentException;
import br.com.skeleton.exception.throwables.SkeletonNotFoundException;
import br.com.skeleton.model.Model;
import br.com.skeleton.repository.SkeletonRepositoryCustom;
import br.com.skeleton.service.SkeletonService;
import br.com.security.model.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SkeletonServiceImpl<T extends Model<ID>, ID extends Serializable> implements SkeletonService<T, ID> {

    private final SkeletonRepositoryCustom<T, ID> repository;
    private final Class<T> clazz;

    @Autowired
    private Validator validator;

    public SkeletonServiceImpl(final SkeletonRepositoryCustom<T, ID> repository, final Class<T> clazz) {
        this.repository = repository;
        this.clazz = clazz;
    }

    @Override
    public final T save(final User user, final T value) {
        //verificando se realmente está criando um novo registro
        if (value.getId() != null) {
            throw new SkeletonBadRequestException(clazz, "id", "Não é possível criar um registro com um id existente");
        }
        value.setUser(user);
        //garantindo que o campo createdBy não ficará nulo
        if (value.getCreatedBy() == null) {
            value.setCreatedBy(user);
        }
        final Date currentTime = new Date();
        value.setDtCreate(currentTime)
                .setLastUpdate(currentTime);
        this.validator.validate(value);
        checkPrecondictionSave(user, value);
        return repository.save(user, value);
    }

    @Override
    public void checkPrecondictionSave(final User user, final T value) {
    }

    @Override
    public final T update(final User user, final T value) {
        //verificando se realmente está alterando um registro
        if (value.getId() == null) {
            throw new SkeletonBadRequestException(clazz, "id", "Não é possível alterar um registro sem inform um id válido");
        }
        value.setUser(user);
        //setando a data da ultima alteração
        value.setLastUpdate(new Date());
        //garantindo que ninguem alterou a data de criação
        value.setDtCreate(this.repository.dtCreateFrom(user, value));
        this.validator.validate(value);
        checkPrecondictionUpdate(user, value);
        checkUser(user, value);
        return repository.save(user, value);
    }

    @Override
    public void checkPrecondictionUpdate(final User user, final T value) {
    }

    @Override
    public T findById(final User user, final ID id) {
        if (!ObjectId.isValid((String) id)) {
            throw new SkeletonNotFoundException(clazz, "id", id + " este registro não foi encontrado");
        }
        final T result = this.repository.findOne(user, id);
        if (result == null) {
            throw new SkeletonNotFoundException(clazz, "id", id + " este registro não foi encontrado");
        }
        return result;
    }

    @Override
    public T findFirst(final User user) {
        final T result = this.repository.findFirst(user);
        if (result == null) {
            throw new SkeletonNotFoundException(clazz, "user", "Nenhum registro encontrado!");
        }
        return result;
    }

    @Override
    public void deleteById(final User user, final ID id) {
        checkPrecondictionDelete(user, id);
        if (!repository.exists(user, id)) {
            throw new SkeletonNotFoundException(clazz, "id", id + " este registro não foi encontrado");
        }
        this.repository.delete(user, id);
        beforeDelete(user, id);
    }

    @Override
    public void delete(final User user, final T value) {
        checkPrecondictionDelete(user, value.getId());
        if (!repository.exists(user, value)) {
            throw new SkeletonNotFoundException(clazz, "id", value.getId() + " este registro não foi encontrado");
        }
        this.repository.delete(user, value);
        beforeDelete(user, value);
    }

    /**
     * Executa alguma regra de negocio após a deleção do registro
     */
    public void beforeDelete(final User user, final ID id) {
    }

    public void beforeDelete(final User user, final T value) {
    }

    @Override
    public void checkPrecondictionDelete(final User user, final ID id) {
        //logger.debug("Não existe pré condições para se deletar o registro");
    }


    @Override
    public Long count(final User user, final Map<String, Object> allRequestParams) {
        return this.repository.count(user, allRequestParams);
    }

    @Override
    public List<T> findAll(final User user, final Map<String, Object> allRequestParams) {
        final List<T> results = this.repository.findAll(user, allRequestParams);
        if (isEmpty(results)) {
            throw new SkeletonNoContentException(clazz, "user", "não foi encontrado nenhum registro");
        }
        return results;
    }

    /**
     * Valida se ouve algum furo no processo de negociocio que venha a se alterar o dono do registro
     */
    private final void checkUser(final User user, final Model value) {

        final Model other = findById(user, (ID) value.getId());
        //não pode-se alterar o usuário
        if (!other.getUser().equals(user)) {
            throw new SkeletonBadRequestException(clazz, "user", "não é possível fazer a alteração do usuário dono do registro");
        }
    }

    protected boolean isEmpty(final List list) {
        return list == null ? true : list.isEmpty();
    }

    /**
     * Valida se uma determinada String é de fato um ObjectId.<br/>
     * Se a String for invalida é lançada um {@link SkeletonNotFoundException}
     *
     * @param id            -> id a ser validado
     * @param clazz         ->modelo da ondem partiu a validação
     * @param pathException ->a qual campo pertence a mensagem
     * @param message       -> mensagem de exception
     * @throws SkeletonNotFoundException
     */
    protected void validateObjectId(final String id, final Class clazz, final String pathException, final String message) {
        if (!ObjectId.isValid(id)) throw new SkeletonNotFoundException(clazz, pathException, message);
    }
}
