package org.geektimes.projects.user.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.exception.UserRegisterException;
import org.geektimes.projects.user.repository.DatabaseUserRepository;
import org.geektimes.projects.user.repository.UserRepository;
import org.geektimes.projects.user.sql.LocalTransactional;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.StringJoiner;

public class UserServiceImpl implements UserService {

    @ConfigProperty(name = "application.name", defaultValue = "default-user-web")
    private String applicationName;

    @Resource(name = "bean/EntityManager")
    private EntityManager entityManager;

    @Resource(name = "bean/UserRepository")
    private UserRepository userRepository;

    @Resource(name = "bean/Validator")
    private Validator validator;

    @PreDestroy
    public void destroy() {
        System.out.println("应用关闭成功：" + applicationName);
    }


    /**
     * 判断是否已注册
     *
     * @see DatabaseUserRepository#selectIsRegistered(java.lang.String)
     * @param user 用户对象
     * @return 是否已注册
     */
    @Override
    public boolean isRegistered(User user) {
        Query query = entityManager.createQuery("SELECT 1 FROM User WHERE name = :name", Integer.class);
        query.setParameter("name", user.getName());
        query.setMaxResults(1);
        return !query.getResultList().isEmpty();
    }

    @Override
    // 默认需要事务
    @LocalTransactional
    public boolean register(User user) {

        // 校验用户是否可注册
        validateRegister(user);

        // before process
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        /**
         * @see DatabaseUserRepository#save(org.geektimes.projects.user.domain.User)
         */
        entityManager.persist(user);

        // TODO 校验ID：必须大于 0 的整数
        /*user.setId(0L);
        Set<ConstraintViolation<User>> violationSet = validator.validateProperty(user, "id");
        if (!violationSet.isEmpty()) {
            throw new UserRegisterException(violationSet.iterator().next().getMessage());
        }*/

        // 调用其他方法方法
        // update(user); // 涉及事务
        // register 方法和 update 方法存在于同一线程
        // register 方法属于 Outer 事务（逻辑）
        // update 方法属于 Inner 事务（逻辑）
        // Case 1 : 两个方法均涉及事务（并且传播行为和隔离级别相同）
        // 两者共享一个物理事务，但存在两个逻辑事务
        // 利用 ThreadLocal 管理一个物理事务（Connection）

        // rollback 情况 1 : update 方法（Inner 事务），它无法主动去调用 rollback 方法
        // 设置 rollback only 状态，Inner TX(rollback only)，说明 update 方法可能存在执行异常或者触发了数据库约束
        // 当 Outer TX 接收到 Inner TX 状态，它来执行 rollback
        // A -> B -> C -> D -> E 方法调用链条
        // A (B,C,D,E) 内联这些方法，合成大方法
        // 关于物理事务是哪个方法创建
        // 其他调用链路事务传播行为是一致时，都是逻辑事务

        // Case 2: register 方法是 PROPAGATION_REQUIRED（事务创建者），update 方法 PROPAGATION_REQUIRES_NEW
        // 这种情况 update 方法也是事务创建者
        // update 方法 rollback-only 状态不会影响 Outer TX，Outer TX 和 Inner TX 是两个物理事务

        // Case 3: register 方法是 PROPAGATION_REQUIRED（事务创建者），update 方法 PROPAGATION_NESTED
        // 这种情况 update 方法同样共享了 register 方法物理事务，并且通过 Savepoint 来实现局部提交和回滚

        // after process
         transaction.commit();

        return true;
    }

    @Override
    public boolean deregister(User user) {
        return false;
    }

    @Override
    @LocalTransactional
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

    /**
     * 验证注册参数
     *
     * @param user 注册用户
     */
    private void validateRegister(User user) {
        // 参数校验
        Set<ConstraintViolation<User>> violationSet = validator.validate(user, User.Register.class);
        if (!violationSet.isEmpty()) {
            StringJoiner errorJoiner = new StringJoiner("; ");;
            for (ConstraintViolation<User> userConstraintViolation : violationSet) {
                String message = userConstraintViolation.getMessage();
                errorJoiner.add(message);
            }
            throw new UserRegisterException(errorJoiner.toString());
        }
        // 校验是否已注册
        if (isRegistered(user)) {
            throw new UserRegisterException(String.format("用户名%s已被注册", user.getName()));
        }
    }

}
