package org.ipman.web.projects.user.service;

import org.ipman.web.projects.user.domain.User;

/**
 * Created by ipipman on 2021/3/10.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.service
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/10 11:35 上午
 */
public interface UserService {

    /**
     * 注册用户
     *
     * @param user 用户对象
     * @return 成功返回<code>true</code>
     */
    boolean register(User user);

    /**
     * 注销用户
     *
     * @param user 用户对象
     * @return 成功返回<code>true</code>
     */
    boolean deregister(User user);

    /**
     * 更新用户信息
     *
     * @param user 用户对象
     * @return
     */
    boolean update(User user);

    User queryUserById(Long id);

    User queryUserByNameAndPassword(String name, String password);
}
