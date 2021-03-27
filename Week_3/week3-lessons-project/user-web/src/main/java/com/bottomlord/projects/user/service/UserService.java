package com.bottomlord.projects.user.service;

import com.bottomlord.projects.user.domain.User;

import java.util.List;

/**
 * @author ChenYue
 * @date 2021/3/2 22:42
 */
public interface UserService {

    /**
     * 注册用户
     * @param user 用户对象
     * @return 成功返回<code>true</code>
     */
    boolean register(User user);

    /**
     * 注销用户
     * @param user 用户对象
     * @return 成功返回<code>true</code>
     */
    boolean deregister(User user);

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return true 更新 false 未更新
     */
    boolean update(User user);

    /**
     * 根据id获取用户对象
     * @param id 用户id
     * @return 用户对象
     */
    User queryUserById(Long id);

    /**
     * 根据用户名和密码获取用户对象
     * @param name 用户名
     * @param password 密码
     * @return 用户对象
     */
    User queryUserByNameAndPassword(String name, String password);

    /**
     * 查询所有用户
     * @return 用户对象集合
     */
    List<User> queryAll();
}
