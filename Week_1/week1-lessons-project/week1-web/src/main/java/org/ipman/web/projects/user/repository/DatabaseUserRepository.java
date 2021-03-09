package org.ipman.web.projects.user.repository;

import org.apache.commons.lang.ClassUtils;
import org.ipman.web.function.ThrowableFunction;
import org.ipman.web.projects.user.domain.User;
import org.ipman.web.projects.user.sql.DBConnectionManager;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.lang.ClassUtils.wrapperToPrimitive;

/**
 * Created by ipipman on 2021/3/3.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.repository
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/3 6:58 下午
 */
public class DatabaseUserRepository implements UserRepository {

    private static Logger logger = Logger.getLogger(DatabaseUserRepository.class.getName());

    /**
     * 通用处理方式
     */
    private static Consumer<Throwable> COMMON_EXCEPTION_HANDLER = e -> logger.log(Level.SEVERE, e.getMessage());

    private final DBConnectionManager dbConnectionManager;

    public DatabaseUserRepository() {
        DBConnectionManager dbConnectionManager = new DBConnectionManager();
        this.dbConnectionManager = dbConnectionManager;
    }

    private Connection getConnection() {
        return dbConnectionManager.getConnection();
    }

    /**
     * 数据类型与 ResultSet 方法名映射
     */
    static Map<Class, String> resultSetMethodMappings = new HashMap<>();

    static Map<Class, String> preparedStatementMethodMappings = new HashMap<>();

    static {
        resultSetMethodMappings.put(Long.class, "getLong");
        resultSetMethodMappings.put(String.class, "getString");

        preparedStatementMethodMappings.put(Long.class, "setLong"); // long
        preparedStatementMethodMappings.put(String.class, "setString"); //

    }

    @Override
    public boolean save(User user) {
        String sql = "INSERT INTO users(name,password,email,phoneNumber) VALUES (" +
                "'" + user.getName() + "'," +
                "'" + user.getPassword() + "'," +
                "'" + user.getEmail() + "'," +
                "'" + user.getPhoneNumber() + "')";
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            System.out.println(statement.executeUpdate(sql));
        }catch (Throwable e){
            e.printStackTrace();
        }
        return false;
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
        return null;
    }

    @Override
    public User getByNameAndPassword(String userName, String password) {
        return executeQuery("SELECT id,name,password,email,phoneNumber FROM users WHERE name=? and password=?",
                resultSet -> {
                    List<User> users = toUserItemList(resultSet);
                    if (!users.isEmpty()) {
                        return users.get(0);
                    } else {
                        return null;
                    }
                }, COMMON_EXCEPTION_HANDLER, userName, password);
    }

    @Override
    public Collection<User> getAll() {
        return executeQuery("SELECT id,name,password,email,phoneNumber FROM users",
                this::toUserItemList, COMMON_EXCEPTION_HANDLER);
    }

    /**
     * 获取查询结果数据
     */
    protected List<User> toUserItemList(ResultSet resultSet)
            throws IntrospectionException, SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
        List<User> users = new ArrayList<>();
        // 游标滚动
        while (resultSet.next()) {
            User user = new User();
            for (PropertyDescriptor propertyDescriptor : userBeanInfo.getPropertyDescriptors()) {
                String fieldName = propertyDescriptor.getName();
                Class<?> fieldType = propertyDescriptor.getPropertyType();
                // 根据字段属性获取数据
                String methodName = resultSetMethodMappings.get(fieldType);
                String columnLabel = fieldName;
                Method resultSetMethod = ResultSet.class.getMethod(methodName, String.class);
                // 通过反射调用 getXXX(String) 方法
                Object resultValue = resultSetMethod.invoke(resultSet, columnLabel);

                // 获取反射 User 类 Setter 方法
                Method setterMethodFromUser = propertyDescriptor.getWriteMethod();
                setterMethodFromUser.invoke(user, resultValue);
            }
            users.add(user);
        }
        return users;
    }

    /**
     * 执行SQL语句
     */
    protected <T> T executeQuery(String sql, ThrowableFunction<ResultSet, T> function,
                                 Consumer<Throwable> exceptionHandler, Object... args) {
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                Class argType = arg.getClass();
                Class wrapperType = wrapperToPrimitive(argType);
                if (wrapperType == null) {
                    wrapperType = argType;
                }

                // Boolean -> boolean
                String methodName = preparedStatementMethodMappings.get(argType);
                Method method = preparedStatement.getClass().getMethod(methodName, wrapperType);

                method.invoke(preparedStatement, i + 1, args);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            // 返回一个 POJO List -> ResultSet -> POJO List
            // ResultSet -> T
            return function.apply(resultSet);

        } catch (Throwable e) {
            e.printStackTrace();
            exceptionHandler.accept(e);
        }
        return null;
    }
}
