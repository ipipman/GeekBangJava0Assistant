package com.bottomlord.configuration.microprofile.config.source;

import java.util.Map;

/**
 * @author ChenYue
 * @date 2021/3/19 17:13
 */
public class OperationSystemEnvironmentVariablesConfigSource extends MapBasedConfigSource {
    public OperationSystemEnvironmentVariablesConfigSource() {
        super("Operation System Environment Variables", 300);
    }

    @Override
    protected void prepareConfig(Map<String, String> map) throws Throwable {
        map.putAll(System.getenv());
    }
}
