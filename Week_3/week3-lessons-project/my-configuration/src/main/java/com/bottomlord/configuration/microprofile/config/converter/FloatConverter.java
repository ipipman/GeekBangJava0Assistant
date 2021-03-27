package com.bottomlord.configuration.microprofile.config.converter;

/**
 * @author ChenYue
 * @date 2021/3/20 12:54
 */
public class FloatConverter extends AbstractConverter<Float> {
    @Override
    protected Float doConvert(String value) {
        return Float.parseFloat(value);
    }
}
