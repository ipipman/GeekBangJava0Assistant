package org.geektimes.context.servlet;

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
 * @since 2021/3/23
 *
 */
public class ServletComponentContextInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        System.out.println("进入了ServletComponentContextInitializer");
        servletContext.addListener(ComponentContextInitializerListener.class);
    }

}