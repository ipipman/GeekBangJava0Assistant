package org.ipman.web.projects.user.listener;

import org.ipman.web.context.ComponentContext;
import org.ipman.web.projects.user.domain.User;
import org.ipman.web.projects.user.management.UserManager;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.management.ManagementFactory;

/**
 * Created by ipipman on 2021/3/10.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.listener
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/10 3:16 下午
 */

// 初始化器
public class ComponentContextInitializerListener implements ServletContextListener {

    private ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        this.servletContext = servletContextEvent.getServletContext();
        // 初始化组件
        ComponentContext componentContext = new ComponentContext();
        componentContext.init(this.servletContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // 销毁阶段
        ComponentContext.getInstance().processPreDestroy();
    }
}

