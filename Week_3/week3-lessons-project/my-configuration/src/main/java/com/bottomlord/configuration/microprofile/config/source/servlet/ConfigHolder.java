package com.bottomlord.configuration.microprofile.config.source.servlet;

import org.eclipse.microprofile.config.Config;

public class ConfigHolder {
    private final static ThreadLocal<Config> CONFIG_THREAD_LOCAL = new ThreadLocal<>();

    public static Config get() {
        return CONFIG_THREAD_LOCAL.get();
    }

    static void set(Config config) {
        CONFIG_THREAD_LOCAL.set(config);
    }

    public static void remove() {
        CONFIG_THREAD_LOCAL.remove();
    }
}
