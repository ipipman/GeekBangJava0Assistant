package org.ipman.web.projects.user.management;

import org.ipman.web.projects.user.domain.User;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Created by ipipman on 2021/3/18.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.manager
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/18 6:28 下午
 */
public class UserMBeanDemo {

    public static void main(String[] args) throws Exception {
        // 获取平台 MBean Server
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        // 为 UserMXBean 定义 ObjectName
        ObjectName objectName = new ObjectName("org.ipman.web.projects.user.management:type=User");

        // 创建 UserMBean 实例
        User user = new User();
        mBeanServer.registerMBean(createUserMBean(user), objectName);

        while (true){
            Thread.sleep(2000);
            System.out.println(user);
        }
    }

    private static Object createUserMBean(User user) throws Exception {
        return new UserManager(user);
    }
}
