package com.bottomlord.configuration.microprofile.config.converter;

/**
 * @author ChenYue
 * @date 2021/3/20 12:54
 */
public class ByteConverter extends AbstractConverter<Byte> {
    @Override
    protected Byte doConvert(String value) {
        return Byte.parseByte(value);
    }
}
