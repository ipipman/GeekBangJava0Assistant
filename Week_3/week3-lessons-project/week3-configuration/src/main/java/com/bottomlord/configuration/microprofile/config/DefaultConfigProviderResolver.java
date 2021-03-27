package com.bottomlord.configuration.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ChenYue
 * @date 2021/3/18 20:10
 */
public class DefaultConfigProviderResolver extends ConfigProviderResolver {
    private ConcurrentHashMap<ClassLoader, Config> configsRepository = new ConcurrentHashMap<>();

    @Override
    public Config getConfig() {
        return getConfig(null);
    }

    @Override
    public Config getConfig(ClassLoader loader) {
        return configsRepository.computeIfAbsent(loader, this::newConfig);
    }

    @Override
    public ConfigBuilder getBuilder() {
        return newConfigBuilder(null);
    }

    @Override
    public void registerConfig(Config config, ClassLoader classLoader) {
        this.configsRepository.put(classLoader, config);
    }

    @Override
    public void releaseConfig(Config config) {
        List<ClassLoader> targetClasses = new ArrayList<>();
        for (Map.Entry<ClassLoader, Config> entry : configsRepository.entrySet()) {
            if (Objects.equals(entry.getValue(), config)) {
                targetClasses.add(entry.getKey());
            }
        }
        targetClasses.forEach(classLoader -> configsRepository.remove(classLoader));
    }

    private ClassLoader resolveClassLoader(ClassLoader classLoader) {
        return classLoader == null ? this.getClass().getClassLoader() : classLoader;
    }

    private ConfigBuilder newConfigBuilder(ClassLoader classLoader) {
        return new DefaultConfigBuilder(resolveClassLoader(classLoader));
    }

    private Config newConfig(ClassLoader classLoader) {
        return newConfigBuilder(classLoader).build();
    }
}
