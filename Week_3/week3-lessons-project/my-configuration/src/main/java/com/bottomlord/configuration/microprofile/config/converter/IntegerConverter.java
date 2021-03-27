package com.bottomlord.configuration.microprofile.config.converter;

/**
 * @author ChenYue
 * @date 2021/3/20 12:51
 */
public class IntegerConverter extends AbstractConverter<Integer> {
    @Override
    protected Integer doConvert(String value) {
        return Integer.parseInt(value);
    }
}
