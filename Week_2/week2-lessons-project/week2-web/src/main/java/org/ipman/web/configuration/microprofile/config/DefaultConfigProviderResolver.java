package org.ipman.web.configuration.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by ipipman on 2021/3/18.
 *
 * @version V1.0
 * @Package org.ipman.web.configuration.microprofile.config
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/18 3:41 下午
 */

// 自定义 配置 解析器
public class DefaultConfigProviderResolver extends ConfigProviderResolver {

    private Config config;

    // 将 Config 在 JNDI 容器中初始化
    @PostConstruct
    public void init() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        config = getConfig(classLoader);
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public Config getConfig(ClassLoader loader) {
        // 判断 ClassLoader 是否为空，把当前 线程的 ClassLoader 作为默认的类加载器
        ClassLoader classLoader = loader;
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }

        // 通过 SPI 加载 Microprofile Config 配置
        ServiceLoader<Config> serviceLoader = ServiceLoader.load(Config.class, classLoader);
        Iterator<Config> iterator = serviceLoader.iterator();
        if (iterator.hasNext()) {
            // 获取第一个 Microprofile Config SPI 第一个实现
            return iterator.next();
        }
        throw new IllegalStateException("No Config implementation found!");
    }

    @Override
    public ConfigBuilder getBuilder() {
        return null;
    }

    @Override
    public void registerConfig(Config config, ClassLoader classLoader) {

    }

    @Override
    public void releaseConfig(Config config) {

    }
}
