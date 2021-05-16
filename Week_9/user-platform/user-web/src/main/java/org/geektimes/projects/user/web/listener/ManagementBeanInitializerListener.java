package org.geektimes.projects.user.web.listener;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.management.UserManager;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.management.ManagementFactory;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @since 2021/3/14
 */
public class ManagementBeanInitializerListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            registerMBean();
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private void registerMBean() throws Exception {
        // 获取平台 MBean Server
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        // 为 UserMXBean 定义 ObjectName
        ObjectName objectName = new ObjectName("org.geektimes.projects.user.management:type=User");
        // 创建 UserMBean 实例
        mBeanServer.registerMBean(new UserManager(new User()), objectName);
    }


    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}