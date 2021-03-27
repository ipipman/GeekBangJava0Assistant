package com.bottomlord.configuration.microprofile.config.converter;

/**
 * @author ChenYue
 * @date 2021/3/20 12:54
 */
public class LongConverter extends AbstractConverter<Long> {
    @Override
    protected Long doConvert(String value) {
        return Long.parseLong(value);
    }
}
