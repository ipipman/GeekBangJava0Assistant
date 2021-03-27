package com.bottomlord.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Map;

/**
 * @author ChenYue
 * @date 2021/3/18 20:59
 */
public class DynamicConfigSource extends MapBasedConfigSource {
    private Map<String, String> config;

    public DynamicConfigSource() {
        super("dynamic", 500);
    }

    @Override
    protected void prepareConfig(Map<String, String> map) throws Throwable {
        this.config = map;
    }

    public void update() {
        //todo 异步更新
    }
}
