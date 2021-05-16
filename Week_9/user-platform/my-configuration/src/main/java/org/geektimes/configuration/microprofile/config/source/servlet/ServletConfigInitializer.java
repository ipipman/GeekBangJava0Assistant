package org.geektimes.configuration.microprofile.config.source.servlet;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

public class ServletConfigInitializer implements ServletContainerInitializer {

    private static final String CONFIG_NAME = Config.class.getName();

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {
        System.out.println("进入了ServletConfigInitializer");
        // 保证在所有ServletContextListener执行前运行
        // 避免了执行ServletContextListener时Config未初始化
        initServletContextConfig(servletContext);
    }

    private void initServletContextConfig(ServletContext servletContext) {
        //private static ThreadLocal<String> THREAD_LOCAL = new InheritableThreadLocal<>();
        ServletContextConfigSource servletContextConfigSource = new ServletContextConfigSource(servletContext);
        // 获取当前 ClassLoader
        ClassLoader classLoader = servletContext.getClassLoader();
        ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
        ConfigBuilder configBuilder = configProviderResolver.getBuilder();
        // 配置 ClassLoader
        configBuilder.forClassLoader(classLoader);
        // 默认配置源（内建的，静态的）
        configBuilder.addDefaultSources();
        // 通过发现配置源（动态的）
        configBuilder.addDiscoveredConverters();
        // 增加扩展配置源（基于 Servlet 引擎）
        configBuilder.withSources(servletContextConfigSource);
        // 获取 Config
        Config config = configBuilder.build();
        // 注册 Config 关联到当前 ClassLoader
        configProviderResolver.registerConfig(config, classLoader);
        servletContext.setAttribute(CONFIG_NAME, config);
    }

}
