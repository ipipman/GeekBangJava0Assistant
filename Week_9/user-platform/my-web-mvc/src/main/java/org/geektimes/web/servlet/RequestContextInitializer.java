package org.geektimes.web.servlet;

import org.geektimes.web.servlet.filter.RequestContextFilter;
import org.geektimes.web.servlet.listener.RequestContextListener;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @since 2021/3/24
 */
public class RequestContextInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        System.out.println("进入了RequestContextInitializer");
        ctx.addListener(RequestContextListener.class);
        FilterRegistration.Dynamic filter = ctx.addFilter("requestContextFilter", new RequestContextFilter());
        filter.addMappingForUrlPatterns(null, false, "/*");
    }

}