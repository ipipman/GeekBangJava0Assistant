package com.bottomlord.configuration.microprofile.config.converter;

/**
 * @author ChenYue
 * @date 2021/3/20 13:00
 */
public class ShortConverter extends AbstractConverter<Short> {
    @Override
    protected Short doConvert(String value) {
        return Short.parseShort(value);
    }
}
