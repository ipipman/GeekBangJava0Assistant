package com.bottomlord.projects.user.management;

import com.bottomlord.projects.user.domain.User;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

/**
 * @author ChenYue
 * @date 2021/3/16 20:25
 */
public class StandardMBeanDemo {
    public static void main(String[] args) throws NotCompliantMBeanException {
        StandardMBean standardMBean = new StandardMBean(new UserManager(new User()), UserManagerMBean.class);
        System.out.println(standardMBean.getMBeanInfo());
    }
}
