package com.bottomlord.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author ChenYue
 * @date 2021/3/20 12:47
 */
public abstract class AbstractConverter<T> implements Converter<T> {
    @Override
    public T convert(String value) throws IllegalArgumentException, NullPointerException {
        if (value == null) {
            throw new NullPointerException("value must not be null!");
        }
        return doConvert(value);
    }

    protected abstract T doConvert(String value);
}
