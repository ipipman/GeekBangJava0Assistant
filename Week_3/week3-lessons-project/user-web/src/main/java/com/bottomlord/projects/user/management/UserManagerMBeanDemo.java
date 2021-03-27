package com.bottomlord.projects.user.management;

import com.bottomlord.projects.user.domain.User;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @author ChenYue
 * @date 2021/3/16 20:14
 */
public class UserManagerMBeanDemo {
    public static void main(String[] args) throws Exception {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("com.bottomlord.projects.user.management:type=User");
        User user = new User();
        mBeanServer.registerMBean(createMBean(user), objectName);
        while (true) {
            Thread.sleep(5000);
            System.out.println(user);
        }
    }

    private static Object createMBean(User user) {
        return new UserManager(user);
    }
}
