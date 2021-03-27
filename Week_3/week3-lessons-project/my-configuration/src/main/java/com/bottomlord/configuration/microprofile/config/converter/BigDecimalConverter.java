package com.bottomlord.configuration.microprofile.config.converter;

import java.math.BigDecimal;

/**
 * @author ChenYue
 * @date 2021/3/24 22:34
 */
public class BigDecimalConverter extends AbstractConverter<BigDecimal> {
    @Override
    protected BigDecimal doConvert(String value) {
        return new BigDecimal(value);
    }
}
