package org.geektimes.projects.user.repository;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.exception.UserRegisterException;

import java.sql.Connection;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseUserRepository extends DatabaseRepository<User> implements UserRepository {

    private static Logger logger = Logger.getLogger(DatabaseUserRepository.class.getName());

    /**
     * 通用处理方式
     */
    private static Consumer<Throwable> COMMON_EXCEPTION_HANDLER = e -> logger.log(Level.SEVERE, e.getMessage());

    public static final String INSERT_USER_DML_SQL =
            "INSERT INTO users(name,password,email,phoneNumber) VALUES (?,?,?,?)";

    public static final String QUERY_ALL_USERS_DML_SQL = "SELECT id,name,password,email,phoneNumber FROM users";

    public static final String QUERY_USER_BY_ID_DML_SQL = "SELECT id,name,password,email,phoneNumber FROM users WHERE id=?";

    public static final String QUERY_USER_BY_NAME_AND_PASSWORD_DML_SQL = "SELECT id,name,password,email,phoneNumber FROM users WHERE name=? and password=?";

    public static final String QUERY_USER_IS_REGISTERED_DML_SQL = "SELECT 1 FROM users WHERE name = ?";

    private Connection getConnection() {
        return dbConnectionManager.getConnection();
    }

    @Override
    public boolean save(User user) {
        return executeUpdate(INSERT_USER_DML_SQL, COMMON_EXCEPTION_HANDLER,
                user.getName(), user.getPassword(), user.getEmail(), user.getPhoneNumber()) > 0;
    }

    @Override
    public boolean deleteById(Long userId) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User getById(Long userId) {
        return queryForObject(QUERY_USER_BY_ID_DML_SQL, COMMON_EXCEPTION_HANDLER, userId);
    }

    @Override
    public User getByNameAndPassword(String userName, String password) {
        return queryForObject(QUERY_USER_BY_NAME_AND_PASSWORD_DML_SQL
                , COMMON_EXCEPTION_HANDLER, userName, password);
    }

    @Override
    public Collection<User> getAll() {
        return queryForList(QUERY_ALL_USERS_DML_SQL, COMMON_EXCEPTION_HANDLER);
    }

    @Override
    public boolean selectIsRegistered(String name) {
        return Objects.equals(queryForObject(QUERY_USER_IS_REGISTERED_DML_SQL, Integer.class,
                e -> {
                    logger.log(Level.SEVERE, "[用户注册] 查询用户是否已注册异常", e);
                    throw new UserRegisterException("注册异常，服务不可用");
                }, name), 1);
    }

}
