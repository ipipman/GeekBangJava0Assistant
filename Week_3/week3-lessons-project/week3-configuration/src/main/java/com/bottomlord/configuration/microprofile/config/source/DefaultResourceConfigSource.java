package com.bottomlord.configuration.microprofile.config.source;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author ChenYue
 * @date 2021/3/18 20:10
 */
public class DefaultResourceConfigSource extends MapBasedConfigSource {
    private static final String CONFIG_PROPERTIES_LOCATION = "config.properties";
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public DefaultResourceConfigSource() {
        super("default resource", 100);
    }

    @Override
    protected void prepareConfig(Map<String, String> map) throws Throwable {
        ClassLoader classLoader = DefaultResourceConfigSource.class.getClassLoader();
        URL resource = classLoader.getResource(CONFIG_PROPERTIES_LOCATION);
        if (resource == null) {
            logger.info("the default config properties can't be found: " + CONFIG_PROPERTIES_LOCATION);
            return;
        }

        try(InputStream inputStream = resource.openStream()) {
            Properties properties = new Properties();
            properties.load(inputStream);
            properties.forEach((k, v) -> {
                map.put(k.toString(), v.toString());
            });
        }
    }
}
