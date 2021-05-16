package org.geektimes.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 基于 Map 数据结构 {@link ConfigSource} 实现
 */
public abstract class MapDataBasedConfigSource<T> implements ConfigSource {

    private final String name;

    private final int ordinal;

    private Map<String, String> source;

    protected MapDataBasedConfigSource(String name, int ordinal, T t) {
        this.name = name;
        this.ordinal = ordinal;
        this.source = getProperties(t);
    }

    /**
     * 获取配置数据 Map
     *
     * @return 不可变 Map 类型的配置数据
     */
    public final Map<String, String> getProperties(T t) {
        Map<String,String> configData = new HashMap<>();
        if (t != null) {
            try {
                prepareConfigData(configData, t);
            } catch (Throwable cause) {
                throw new IllegalStateException("准备配置数据发生错误",cause);
            }
        }
        return Collections.unmodifiableMap(configData);
    }

    /**
     * 获取配置数据 Map
     */
    public final Map<String, String> getProperties() {
        return this.source;
    }

    /**
     * 准备配置数据
     * @param configData
     * @throws Throwable
     */
    protected abstract void prepareConfigData(Map configData, T t) throws Throwable;

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final int getOrdinal() {
        return ordinal;
    }

    @Override
    public Set<String> getPropertyNames() {
        return source.keySet();
    }

    @Override
    public String getValue(String propertyName) {
        return source.get(propertyName);
    }

}
