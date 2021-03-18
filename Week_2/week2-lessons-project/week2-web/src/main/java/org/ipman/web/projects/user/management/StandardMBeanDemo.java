package org.ipman.web.projects.user.management;

import org.ipman.web.projects.user.domain.User;

import javax.management.MBeanInfo;
import javax.management.StandardMBean;

/**
 * Created by ipipman on 2021/3/18.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.management
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/18 6:41 下午
 */
public class StandardMBeanDemo {

    public static void main(String[] args) throws Exception {

        // 将静态的 MBean 接口转换为 DynamicMBean
        StandardMBean standardMBean = new StandardMBean(new UserManager(new User()), UserManagerMBean.class);

        MBeanInfo mBeanInfo = standardMBean.getMBeanInfo();

        System.out.println(mBeanInfo);
    }
}
