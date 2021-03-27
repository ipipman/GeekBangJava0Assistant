package com.bottomlord.configuration.microprofile.config.source.servlet;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author ChenYue
 * @date 2021/3/24 22:22
 */
public class ServletContextConfigInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        ServletContextConfigSource contextConfigSource = new ServletContextConfigSource(servletContext);
        ClassLoader classLoader = servletContext.getClassLoader();
        ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
        ConfigBuilder configBuilder = configProviderResolver.getBuilder();
        configBuilder.forClassLoader(classLoader);
        configBuilder.addDefaultSources();
        configBuilder.addDiscoveredSources();
        configBuilder.addDiscoveredConverters();
        configBuilder.withSources(contextConfigSource);
        Config config = configBuilder.build();
        configProviderResolver.registerConfig(config, classLoader);
        servletContext.setAttribute("servletContextConfig", config);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
