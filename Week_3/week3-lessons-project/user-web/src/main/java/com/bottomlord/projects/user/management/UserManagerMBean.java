package com.bottomlord.projects.user.management;

import com.bottomlord.projects.user.domain.User;

/**
 * @author ChenYue
 * @date 2021/3/16 20:10
 * {@link User}
 */
public interface UserManagerMBean {
    /**
     * MBeanAttributeInfo 列表
     */
    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    String getPassword();

    void setPassword(String password);

    String getEmail();

    void setEmail(String email);

    String getPhoneNumber();

    void setPhoneNumber(String phoneNumber);

    /**
     * MBeanOperationInfo
     */
    @Override
    String toString();
}
