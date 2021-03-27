package com.bottomlord.configuration.microprofile.config.converter;

import java.math.BigInteger;

/**
 * @author ChenYue
 * @date 2021/3/24 22:37
 */
public class BigIntegerConverter extends AbstractConverter<BigInteger> {
    @Override
    protected BigInteger doConvert(String value) {
        return new BigInteger(value);
    }
}
