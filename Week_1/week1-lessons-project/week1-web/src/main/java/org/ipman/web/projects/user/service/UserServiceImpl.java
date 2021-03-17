package org.ipman.web.projects.user.service;

import org.ipman.web.projects.user.domain.User;
import org.ipman.web.projects.user.repository.DatabaseUserRepository;

import javax.annotation.Resource;

/**
 * Created by ipipman on 2021/3/10.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.service
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/10 11:35 上午
 */
public class UserServiceImpl implements UserService {

    @Resource(name = "bean/DatabaseUserRepository")
    private DatabaseUserRepository databaseUserRepository;

    @Override
    public boolean register(User user) {
        return databaseUserRepository.save(user);
    }

    @Override
    public boolean deregister(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User queryUserById(Long id) {
        return null;
    }

    @Override
    public User queryUserByNameAndPassword(String name, String password) {
        return null;
    }
}
