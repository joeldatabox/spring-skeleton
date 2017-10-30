package br.com.skeleton.config.mongo.converters;


import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Component
public class Decimal128ToBigDecimalConverter implements Converter<Decimal128, BigDecimal> {

    @Override
    public BigDecimal convert(final Decimal128 source) {
        return source == null ? null : source.bigDecimalValue();
    }
}