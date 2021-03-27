package com.bottomlord.projects.user.service;

import com.bottomlord.projects.user.domain.User;
import com.bottomlord.projects.user.repository.DatabaseUserRepository;
import com.bottomlord.projects.user.validator.DelegatingValidator;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ChenYue
 * @date 2021/3/3 20:40
 */
public class UserServiceImpl implements UserService {
    @Resource(name = "bean/Repository")
    private DatabaseUserRepository userRepository;

    @Resource(name = "bean/EntityManager")
    private EntityManager entityManager;

    @Resource(name = "bean/Validator")
    private DelegatingValidator validator;


    @Override
    public boolean register(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.size() > 0) {
            throw new RuntimeException(violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",")));
        }

        return userRepository.save(user);
    }

    @Override
    public boolean deregister(User user) {
        return userRepository.deleteById(user.getId());
    }

    @Override
    public boolean update(User user) {
        return userRepository.update(user);
    }

    @Override
    public User queryUserById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public User queryUserByNameAndPassword(String name, String password) {
        return userRepository.getByNameAndPassword(name, password);
    }

    @Override
    public List<User> queryAll() {
        return new ArrayList<>(userRepository.getAll());
    }
}
