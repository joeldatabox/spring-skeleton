package br.com.skeleton.repository;

import br.com.security.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@NoRepositoryBean
public interface SkeletonRepositoryCustom<T, ID extends Serializable> extends MongoRepository<T, ID> {

    T save(final User user, final T value);

    T findOne(final User user, final ID id);

    T findFirst(final User user);

    void delete(final User user, final ID id);

    void delete(final User user, final T value);

    List<T> findAll(final User user, final Map<String, Object> queryParams);

    long count(final User user, final Map<String, Object> queryParams);

    boolean exists(final User user, final T value);

    boolean exists(final User user, final ID value);

    Date dtCreateFrom(final User user, final T value);

    Date dtCreateFrom(final User user, final ID value);
}
