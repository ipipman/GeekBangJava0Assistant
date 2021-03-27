package com.bottomlord.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author ChenYue
 * @date 2021/3/19 12:43
 */
public class ConfigSources implements Iterable<ConfigSource> {
    private boolean addedDefaultConfigSources;

    private boolean addedDiscoveredConfigSources;

    private final List<ConfigSource> configSources = new LinkedList<>();

    private ClassLoader classLoader;

    public ConfigSources(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public synchronized void addDefaultConfigSources() {
        if (addedDefaultConfigSources) {
            return;
        }

        addConfigSources(JavaSystemPropertiesConfigSource.class,
                        OperationSystemEnvironmentVariablesConfigSource.class,
                        DefaultResourceConfigSource.class);

        addedDefaultConfigSources = true;
    }

    public synchronized void addDiscoveredSources() {
        if (addedDiscoveredConfigSources) {
            return;
        }

        addConfigSources(ServiceLoader.load(ConfigSource.class, classLoader));
        addedDiscoveredConfigSources = true;
    }

    @Override
    public Iterator<ConfigSource> iterator() {
        return configSources.iterator();
    }

    @SafeVarargs
    private final void addConfigSources(Class<? extends ConfigSource>... configSources) {
        addConfigSources(Stream.of(configSources).map(this::newInstance).toArray(ConfigSource[]::new));
    }

    public void addConfigSources(ConfigSource... configSources) {
        addConfigSources(Arrays.asList(configSources));
    }

    private void addConfigSources(Iterable<ConfigSource> configSources) {
        configSources.forEach(this.configSources::add);
        this.configSources.sort(ConfigSourceOrdinalComparator.INSTANCE);
    }

    private ConfigSource newInstance(Class<? extends ConfigSource> configSourceClass) {
        try {
            return configSourceClass.getConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new IllegalStateException();
        }
    }
}
