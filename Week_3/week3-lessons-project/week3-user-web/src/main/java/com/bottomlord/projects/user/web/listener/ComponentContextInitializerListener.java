package com.bottomlord.projects.user.web.listener;

import com.bottomlord.context.ClassicComponentContext;
import com.bottomlord.functions.ThrowableFunction;
import com.bottomlord.projects.user.domain.User;
import com.bottomlord.projects.user.management.UserManager;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.management.ManagementFactory;

/**
 * @author liveForExp
 */
@WebListener
public class ComponentContextInitializerListener implements ServletContextListener {
    private ServletContext servletContext;

    /**
     * 由main方法调用，此时没有流量进来，不需要考虑多线程
     * @param sce sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
        ClassicComponentContext context = new ClassicComponentContext();
        servletContext.log("初始化ComponentContext!");
        context.init(this.servletContext);
        registerJolokiaMBean();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    private void registerJolokiaMBean() {
        User user = new User();
        ThrowableFunction.execute(user, u -> {
            ObjectName objectName = new ObjectName("jolokia:name=User");
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            mBeanServer.registerMBean(new UserManager(u), objectName);
            return null;
        });
    }
}