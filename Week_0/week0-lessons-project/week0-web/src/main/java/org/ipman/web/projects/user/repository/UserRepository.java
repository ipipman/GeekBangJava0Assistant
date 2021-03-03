package org.ipman.web.projects.user.repository;

import org.ipman.web.projects.user.domain.User;

import java.util.Collection;

/**
 * Created by ipipman on 2021/3/3.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.repository
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/3 6:58 下午
 */
public interface UserRepository {

    boolean save(User user);

    boolean deleteById(Long userId);

    boolean update(User user);

    User getById(Long userId);

    User getByNameAndPassword(String userName, String password);

    Collection<User> getAll();
}
