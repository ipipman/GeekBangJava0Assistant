package org.geektimes.projects.user.repository;

import org.geektimes.projects.user.domain.User;

import java.util.Collection;

/**
 * 用户存储仓库
 *
 * @since 1.0
 */
public interface UserRepository {

    boolean save(User user);

    boolean deleteById(Long userId);

    boolean update(User user);

    User getById(Long userId);

    User getByNameAndPassword(String userName, String password);

    Collection<User> getAll();

    /**
     * 查询用户是否注册
     *
     * @param name 用户名
     */
    boolean selectIsRegistered(String name);

}
