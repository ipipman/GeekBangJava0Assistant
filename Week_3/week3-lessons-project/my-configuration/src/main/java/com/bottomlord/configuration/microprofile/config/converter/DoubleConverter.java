package com.bottomlord.configuration.microprofile.config.converter;

/**
 * @author ChenYue
 * @date 2021/3/20 12:54
 */
public class DoubleConverter extends AbstractConverter<Double> {
    @Override
    protected Double doConvert(String value) {
        return Double.parseDouble(value);
    }
}
