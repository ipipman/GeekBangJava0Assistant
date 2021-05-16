package org.geektimes.configuration.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;
import org.geektimes.configuration.microprofile.config.converter.Converters;
import org.geektimes.configuration.microprofile.config.source.servlet.RequestContextConfigSource;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 请求上下文配置
 * <p>
 *
 * @author liuwei
 * @since 2021/3/24
 * @see RequestContextConfigSource
 */
public class RequestContextConfig implements Config {

    /**
     * @see RequestContextConfig#doGetConverter(java.lang.Class)
     */
    private static volatile Converters converters;

    RequestContextConfigSource configSource;

    private final static ThreadLocal<RequestContextConfig> requestContexts = new ThreadLocal<>();

    public RequestContextConfig(HttpServletRequest request, HttpServletResponse response) {
        this.configSource = new RequestContextConfigSource(request, response);
        requestContexts.set(this);
    }

    public static RequestContextConfig build(HttpServletRequest request, HttpServletResponse response) {
        return new RequestContextConfig(request, response);
    }


    public static RequestContextConfig get() {
        return Objects.requireNonNull(requestContexts.get());
    }

    public static void destroy() {
        requestContexts.remove();
    }

    public final HttpServletRequest getRequest() {
        return configSource.getRequest();
    }

    public final HttpServletResponse getResponse() {
        return configSource.getResponse();
    }

    public final ServletContext getServletContext() {
        return configSource.getServletContext();
    }

    public String getValue(String propertyName) {
        return getValue(propertyName, String.class);
    }

    public String getMethod() {
        return getRequest().getMethod();
    }

    @Override
    public <T> T getValue(String propertyName, Class<T> propertyType) {
        String value = configSource.getValue(propertyName);
        if (value != null) {
            // String 转换成目标类型
            Converter<T> converter = doGetConverter(propertyType);
            if (converter == null) {
                // 没有转换器支持该类型
                throw new IllegalArgumentException("No Converter supports for " + propertyType.getName());
            }
            return converter.convert(value);
        }
        return null;
    }

    @Override
    @Deprecated
    public ConfigValue getConfigValue(String propertyName) {
        return null;
    }

    @Override
    public <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType) {
        T value = getValue(propertyName, propertyType);
        return Optional.ofNullable(value);
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return configSource.getPropertyNames();
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        List<ConfigSource> configSourceList = new ArrayList<>(1);
        configSourceList.add(configSource);
        return Collections.unmodifiableList(configSourceList);
    }

    @Override
    public <T> Optional<Converter<T>> getConverter(Class<T> forType) {
        Converter converter = doGetConverter(forType);
        return converter == null ? Optional.empty() : Optional.of(converter);
    }

    /**
     * 获取类型转换器
     */
    protected <T> Converter<T> doGetConverter(Class<T> forType) {
        // 单例懒加载
        if (RequestContextConfig.converters == null) {
            synchronized (RequestContextConfig.class) {
                if (RequestContextConfig.converters == null) {
                    RequestContextConfig.converters = new Converters();
                    RequestContextConfig.converters.addDiscoveredConverters();
                }
            }
        }
        // 获取类型转换器
        List<Converter> converters = RequestContextConfig.converters.getConverters(forType);
        return converters.isEmpty() ? null : converters.get(0);
    }

    @Override
    @Deprecated
    public <T> T unwrap(Class<T> type) {
        return null;
    }

}