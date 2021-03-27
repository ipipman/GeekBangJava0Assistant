package com.bottomlord.configuration.microprofile.config.source.servlet;

import com.bottomlord.configuration.microprofile.config.source.MapBasedConfigSource;

import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author ChenYue
 * @date 2021/3/24 22:23
 */
public class ServletContextConfigSource extends MapBasedConfigSource {
    private final ServletContext servletContext;

    public ServletContextConfigSource(ServletContext servletContext) {
        super("servlet context config source", 600);
        this.servletContext = servletContext;
    }

    @Override
    protected void prepareConfig(Map<String, String> map) throws Throwable {
        Enumeration<String> names = servletContext.getInitParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            map.put(name, servletContext.getInitParameter(name));
        }
    }
}
