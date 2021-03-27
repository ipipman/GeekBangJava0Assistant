package com.bottomlord.configuration.microprofile.config.source;

import java.util.Map;

/**
 * @author ChenYue
 * @date 2021/3/16 12:19
 */
public class JavaSystemPropertiesConfigSource extends MapBasedConfigSource {
    public JavaSystemPropertiesConfigSource() {
        super("Java System Properties", 400);
    }

    @Override
    protected void prepareConfig(Map<String, String> map) throws Throwable {
        System.getProperties().forEach((k, v) -> {
            map.put(k.toString(), v.toString());
        });
    }
}
