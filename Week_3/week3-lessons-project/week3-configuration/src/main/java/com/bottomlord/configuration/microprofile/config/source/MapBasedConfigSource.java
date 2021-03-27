package com.bottomlord.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ChenYue
 * @date 2021/3/18 20:14
 */
public abstract class MapBasedConfigSource implements ConfigSource {
    private final String name;
    private final int ordinal;
    private Map<String, String> source;

    public MapBasedConfigSource(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    @Override
    public Set<String> getPropertyNames() {
        return Collections.unmodifiableSet(source.keySet());
    }

    @Override
    public synchronized String getValue(String propertyName) {
        if (this.source == null) {
            this.source = getProperties();
        }
        return this.source.get(propertyName);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> map = new HashMap<>();
        try {
            prepareConfig(map);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
        return Collections.unmodifiableMap(map);
    }

    protected abstract void prepareConfig(Map<String, String> map) throws Throwable;

    @Override
    public int getOrdinal() {
        return this.ordinal;
    }
}
