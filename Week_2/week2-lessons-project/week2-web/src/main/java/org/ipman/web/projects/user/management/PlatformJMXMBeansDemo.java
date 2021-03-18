package org.ipman.web.projects.user.management;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;

/**
 * Created by ipipman on 2021/3/18.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.management
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/18 6:39 下午
 */
public class PlatformJMXMBeansDemo {

    public static void main(String[] args) {
        // 客户端去获取 ClassLoadingMXBean 对象（代理对象）
        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();

        System.out.println(classLoadingMXBean.getLoadedClassCount());
    }
}
