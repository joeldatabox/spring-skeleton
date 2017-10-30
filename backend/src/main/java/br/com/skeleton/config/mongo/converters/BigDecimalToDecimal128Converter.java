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
public class BigDecimalToDecimal128Converter implements Converter<BigDecimal, Decimal128> {

    @Override
    public Decimal128 convert(final BigDecimal source) {
        return source == null ? null : Decimal128.parse(source.toPlainString());
    }
}