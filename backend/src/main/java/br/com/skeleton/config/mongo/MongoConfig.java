package br.com.skeleton.config.mongo;

import br.com.skeleton.config.mongo.converters.BigDecimalToDecimal128Converter;
import br.com.skeleton.config.mongo.converters.Decimal128ToBigDecimalConverter;
import br.com.skeleton.repository.impl.SkeletonRepositoryCustomImpl;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.MongoCredential.createCredential;
import static java.util.Collections.singletonList;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Configuration
@EnableMongoRepositories(basePackages = "br.com", repositoryBaseClass = SkeletonRepositoryCustomImpl.class)
public class MongoConfig extends AbstractMongoConfiguration {
    private final CustomConversions converters;

    private final String dataBaseName;
    private final String hostDataBase;
    private final String portDataBase;
    private final String userName;
    private final String password;

    public MongoConfig(
            @Value("${spring.data.mongodb.database}") final String dataBaseName,
            @Value("${spring.data.mongodb.host}") final String hostDataBase,
            @Value("${spring.data.mongodb.port}") final String portDataBase,
            @Value("${spring.data.mongodb.username}") final String userName,
            @Value("${spring.data.mongodb.password}") final String password) {

        this.dataBaseName = dataBaseName;
        this.hostDataBase = hostDataBase;
        this.portDataBase = portDataBase;
        this.userName = userName;
        this.password = password;

        final List listConverters = new ArrayList(2);
        listConverters.add(new BigDecimalToDecimal128Converter());
        listConverters.add(new Decimal128ToBigDecimalConverter());

        this.converters = new CustomConversions(listConverters);
    }

    @Override
    protected String getDatabaseName() {
        return dataBaseName;
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient(
                singletonList(new ServerAddress(this.hostDataBase, Integer.valueOf(this.portDataBase))),
                singletonList(createCredential(this.userName, this.dataBaseName, password.toCharArray())));
    }

    @Override
    public CustomConversions customConversions() {
        return this.converters;
    }

    @Bean
    public MongoRepositoryFactory getMongoRepositoryFactory() {
        try {
            return new MongoRepositoryFactory(this.mongoTemplate());
        } catch (Exception e) {
            throw new RuntimeException("error creating mongo repository factory", e);
        }
    }

}
