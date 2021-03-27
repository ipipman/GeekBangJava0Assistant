package com.bottomlord.configuration.microprofile.config;

import com.bottomlord.configuration.microprofile.config.converter.Converters;
import com.bottomlord.configuration.microprofile.config.source.ConfigSources;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

import java.util.*;

import static java.util.stream.StreamSupport.stream;

/**
 * @author ChenYue
 * @date 2021/3/18 20:09
 */
public class DefaultConfig implements Config {
    private final ConfigSources configSources;
    private final Converters converters;

    public DefaultConfig(ConfigSources configSources, Converters converters) {
        this.configSources = configSources;
        this.converters = converters;
    }

    @Override
    public <T> T getValue(String propertyName, Class<T> propertyType) {
        String propertyValue = getPropertyValue(propertyName);
        Converter<T> converter = doGetConverter(propertyType);
        return converter == null ? null : converter.convert(propertyValue);
    }

    protected String getPropertyValue(String propertyName) {
        String propertyValue = null;
        for (ConfigSource configSource : configSources) {
            propertyValue = configSource.getValue(propertyName);
            if (propertyValue != null) {
                break;
            }
        }
        return propertyValue;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected <T> Converter<T> doGetConverter(Class<?> propertyType) {
        List<Converter> propertyConverters = converters.getConverters(propertyType);
        return propertyConverters == null ? null : propertyConverters.get(0);
    }

    @Override
    public ConfigValue getConfigValue(String propertyName) {

        String propertyValue = null;

        ConfigSource configSource = null;

        for (ConfigSource source : configSources) {
            configSource = source;
            propertyValue = configSource.getValue(propertyName);
            if (propertyValue != null) {
                break;
            }
        }

        if (propertyValue == null) {
            return null;
        }

        return new DefaultConfigValue(propertyName, propertyValue, transformPropertyValue(propertyValue),
                configSource.getName(),
                configSource.getOrdinal());
    }

    /**
     * 转换属性值
     * @param propertyValue 原属性值
     * @return 转换后的属性值
     */
    protected String transformPropertyValue(String propertyValue) {
        return propertyValue;
    }

    @Override
    public <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType) {
        return Optional.ofNullable(getValue(propertyName, propertyType));
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return stream(configSources.spliterator(), false)
                .map(ConfigSource::getPropertyNames)
                .collect(LinkedHashSet::new, Set::addAll, Set::addAll);
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return configSources;
    }

    @Override
    public <T> Optional<Converter<T>> getConverter(Class<T> forType) {
        Converter<T> converter = doGetConverter(forType);
        return converter == null ? Optional.empty() : Optional.of(converter);
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return null;
    }
}
