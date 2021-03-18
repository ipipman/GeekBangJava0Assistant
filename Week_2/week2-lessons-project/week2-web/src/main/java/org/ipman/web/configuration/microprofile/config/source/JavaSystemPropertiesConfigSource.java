package org.ipman.web.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ipipman on 2021/3/18.
 *
 * @version V1.0
 * @Package org.ipman.web.configuration.microprofile.config.source
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/18 3:41 下午
 */
public class JavaSystemPropertiesConfigSource implements ConfigSource {

    /**
     * Java 系统属性最好通过本地变量保存，使用 Map 保存，尽可能运行期不去调整
     * -Dapplication.name=user-web
     */
    private final Map<String, String> properties;


    public JavaSystemPropertiesConfigSource() {
        Map systemProperties = System.getProperties();
        properties = new HashMap<>(systemProperties);
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public int getOrdinal() {
        return 0;
    }

    @Override
    public String getValue(String propertyName) {
        return properties.get(propertyName);
    }

    @Override
    public String getName() {
        return "Java System Properties";
    }
}
