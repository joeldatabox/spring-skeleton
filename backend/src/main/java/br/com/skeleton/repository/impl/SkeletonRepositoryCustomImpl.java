package br.com.skeleton.repository.impl;


import br.com.security.model.User;
import br.com.skeleton.exception.throwables.repository.SkeletonRepositoryIdIsNullException;
import br.com.skeleton.exception.throwables.repository.SkeletonRepositoryUserNotInformedException;
import br.com.skeleton.model.Model;
import br.com.skeleton.repository.SkeletonRepositoryCustom;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.skeleton.repository.infra.Aggregate.createAggregations;
import static br.com.skeleton.repository.infra.Aggregate.createAggregationsCount;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SkeletonRepositoryCustomImpl<T extends Model<ID>, ID extends Serializable> extends SimpleMongoRepository<T, ID> implements SkeletonRepositoryCustom<T, ID> {
    protected final MongoOperations operations;
    private final Class<T> CLASS;
    private final String COLLECTION;

    public SkeletonRepositoryCustomImpl(final MongoEntityInformation<T, ID> metadata, final MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.operations = mongoOperations;
        this.CLASS = metadata.getJavaType();
        this.COLLECTION = metadata.getCollectionName();
    }

    @Override
    public final T save(final User user, final T value) {
        validateUser(user);
        value.setUser(user);
        return super.save(value);
    }

    @Override
    public final T findOne(final User user, final ID id) {
        validateUser(user);
        validateId(id);
        return operations.findOne(new Query(where("user").is(user).and("id").is(id)), CLASS);
    }

    @Override
    public T findFirst(final User user) {
        validateUser(user);
        return operations.findOne(new Query(where("user").is(user)), CLASS);
    }

    @Override
    public final void delete(final User user, final ID id) {
        validateUser(user);
        validateId(id);
        operations.remove(new Query(where("user").is(user).and("id").is(id)), CLASS);
    }

    @Override
    public final void delete(final User user, final T value) {
        delete(user, value.getId());
    }

    @Override
    public final List<T> findAll(final User user, final Map<String, Object> queryParams) {
        validateUser(user);
        return operations.aggregate(
                newAggregation(
                        createAggregations(
                                CLASS,
                                new HashMap<>(addUserQueryParam(user, queryParams))
                        )
                ),
                COLLECTION, CLASS)
                .getMappedResults();
    }

    @Override
    public final long count(final User user, final Map<String, Object> queryParams) {
        validateUser(user);
        final AggregationResults result = operations.aggregate(
                newAggregation(
                        createAggregationsCount(
                                CLASS,
                                new HashMap<>(addUserQueryParam(user, queryParams))
                        )),
                COLLECTION, ResultCount.class);
        return result.getUniqueMappedResult() != null ? ((ResultCount) result.getUniqueMappedResult()).count : 0;
    }

    @Override
    public final boolean exists(final User user, final T value) {
        return exists(user, value.getId());
    }

    @Override
    public final boolean exists(final User user, final ID id) {
        validateUser(user);
        return operations.exists(new Query(where("user").is(user).and("id").is(id)), CLASS);
    }

    @Override
    public Date dtCreateFrom(final User user, final T value) {
        return dtCreateFrom(user, value.getId());
    }

    @Override
    public Date dtCreateFrom(final User user, final ID value) {
        final AggregationResults result = operations.aggregate(
                newAggregation(
                        match(where("user.$id").is(new ObjectId(user.getId())).and("id").is(new ObjectId((String) value))),
                        project().and("dtCreate").as("dtResult")
                ), COLLECTION, ResultDate.class);

        return result.getUniqueMappedResult() != null ? ((ResultDate) result.getUniqueMappedResult()).getDtResult() : null;
    }

    private final void validateId(final ID id) {
        if (id == null) {
            throw new SkeletonRepositoryIdIsNullException(this.CLASS);
        }
    }

    private final void validateUser(final User user) {
        if (user == null) {
            throw new SkeletonRepositoryUserNotInformedException(this.CLASS);
        }
    }

    private final Map<String, Object> addUserQueryParam(final User user, final Map<String, Object> queryParams) {
        final Map<String, Object> query = new HashMap<>(1);
        query.put("user.$id.$is", new ObjectId(user.getId()));
        if (queryParams != null) {
            query.putAll(queryParams);
        }
        return query;
    }

    private final class ResultCount {
        private Long count;

        public Long getCount() {
            return count;
        }

        public ResultCount setCount(final Long count) {
            this.count = count;
            return this;
        }
    }

    private final class ResultDate {
        private Date dtResult;

        public Date getDtResult() {
            return dtResult;
        }

        public ResultDate setDtResult(final Date dtResult) {
            this.dtResult = dtResult;
            return this;
        }
    }
}