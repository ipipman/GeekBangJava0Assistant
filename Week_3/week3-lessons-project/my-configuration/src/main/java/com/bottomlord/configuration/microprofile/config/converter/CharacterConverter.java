package com.bottomlord.configuration.microprofile.config.converter;

/**
 * @author ChenYue
 * @date 2021/3/24 22:35
 */
public class CharacterConverter extends AbstractConverter<Character> {
    @Override
    protected Character doConvert(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return value.charAt(0);
    }
}
