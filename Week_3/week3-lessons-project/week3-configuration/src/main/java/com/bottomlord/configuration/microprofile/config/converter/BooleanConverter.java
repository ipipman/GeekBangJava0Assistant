package com.bottomlord.configuration.microprofile.config.converter;

/**
 * @author ChenYue
 * @date 2021/3/20 12:47
 */
public class BooleanConverter extends AbstractConverter<Boolean> {
    @Override
    protected Boolean doConvert(String value) {
        return Boolean.parseBoolean(value);
    }
}
