package org.geektimes.configuration.microprofile.config.source.servlet;

import org.eclipse.microprofile.config.spi.ConfigSource;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @since 2021/3/24
 * @see org.geektimes.configuration.microprofile.config.RequestContextConfig
 */
public class RequestContextConfigSource implements ConfigSource {

    private final String name;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public RequestContextConfigSource(HttpServletRequest request, HttpServletResponse response) {
        this.name = request.getRequestURI();
        this.request = request;
        this.response = response;
    }

    public RequestContextConfigSource(String name, HttpServletRequest request, HttpServletResponse response) {
        this.name = name;
        this.request = request;
        this.response = response;
    }

    public final HttpServletRequest getRequest() {
        return this.request;
    }

    public final HttpServletResponse getResponse() {
        return this.response;
    }

    public final ServletContext getServletContext() {
        return this.request.getServletContext();
    }

    @Override
    public Set<String> getPropertyNames() {
        return request.getParameterMap().keySet();
    }

    @Override
    public String getValue(String propertyName) {
        return request.getParameter(propertyName);
    }

    @Override
    public String getName() {
        return this.name;
    }

}