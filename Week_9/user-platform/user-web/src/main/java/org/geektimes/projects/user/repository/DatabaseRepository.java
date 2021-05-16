package org.geektimes.projects.user.repository;

import org.geektimes.context.ClassicComponentContext;
import org.geektimes.projects.user.sql.DBConnectionManager;

import javax.annotation.PostConstruct;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 通用数据库仓库存储
 * <p>
 *
 * @author liuwei
 * @since 2021/3/8
 */
public class DatabaseRepository<T> {

    private BeanInfo beanInfo;
    private Class<T> GenericClazz;
    public DBConnectionManager dbConnectionManager;

    @PostConstruct
    public void initDBConnectionManager() {
        this.dbConnectionManager = ClassicComponentContext.getInstance().getComponent("bean/DBConnectionManager");
        if (this.dbConnectionManager == null) {
            throw new RuntimeException("dbConnectionManager initialization failed");
        }
    }

    public DatabaseRepository() {
        this.GenericClazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            this.beanInfo = Introspector.getBeanInfo(GenericClazz, Object.class);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    public T queryForObject(String sql, Consumer<Throwable> exceptionHandler, Object... args) {
        List<T> results = queryForList(sql, exceptionHandler, args);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<T> queryForList(String sql, Consumer<Throwable> exceptionHandler, Object... args) {
        return queryForList(sql, GenericClazz, exceptionHandler, args);
    }

    public <C> C queryForObject(String sql, Class<C> aClass, Consumer<Throwable> exceptionHandler, Object... args) {
        List<C> results = queryForList(sql, aClass, exceptionHandler, args);
        return results.isEmpty() ? null : results.get(0);
    }

    public <C> List<C> queryForList(String sql, Class<C> aClass, Consumer<Throwable> exceptionHandler, Object... args) {
        try (Connection connection = dbConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                PreparedStatementSetEnum.setStatement(preparedStatement, i + 1, arg);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            return buildResults(resultSet, aClass);
        } catch (Throwable e) {
            exceptionHandler.accept(e);
        }
        return new ArrayList<>(1);
    }


    /**
     * 更新操作
     *
     * @param sql              sql语句
     * @param exceptionHandler 异常处理
     * @param args             参数
     * @return 影响行数
     */
    public int executeUpdate(String sql, Consumer<Throwable> exceptionHandler, Object... args) {
        try (Connection connection = dbConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                PreparedStatementSetEnum.setStatement(preparedStatement, i + 1, arg);
            }
            return preparedStatement.executeUpdate();
        } catch (Throwable e) {
            exceptionHandler.accept(e);
        }
        return 0;
    }

    /**
     * 构建结果集
     *
     * @param resultSet 数据库结果
     * @param aClass    返回类型
     */
    private <C> List<C> buildResults(ResultSet resultSet, Class<C> aClass) throws SQLException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        List<C> results = new ArrayList<>(resultSet.getFetchSize());
        boolean primitive = aClass.isPrimitive();
        boolean isMap = !primitive && Map.class.isAssignableFrom(aClass);
        PropertyDescriptor[] propertyDescriptors = null;
        // 如果为基本类型，就不获取bean字段
        if (!primitive && !isMap) {
            propertyDescriptors = beanInfo.getPropertyDescriptors();
        }
        while (resultSet.next()) {
            // 基本数据类型，获取第一行结果
            if (primitive) {
                Object result = resultSet.getObject(1);
                if (result != null) {
                    Method method = aClass.getMethod("valueOf", String.class);
                    result = method.invoke(null, result.toString());
                    results.add((C) result);
                }
                continue;
            }
            if (isMap) {
                // TODO 暂未做处理
                continue;
            }
            C c = aClass.newInstance();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String fieldName = propertyDescriptor.getName();
                Class<?> propertyType = propertyDescriptor.getPropertyType();
                Object result = ResultGetEnum.getResult(resultSet, propertyType, mapColumnLabel(fieldName));
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(c, result);
            }
            results.add(c);
        }
        return results;
    }

    /**
     * 参数设置枚举
     */
    enum PreparedStatementSetEnum {

        STRING(String.class) {
            @Override
            void doSetStatement(PreparedStatement preparedStatement, int index, Object value) throws SQLException {
                preparedStatement.setString(index, (String) value);
            }
        },
        LONG(Long.class) {
            @Override
            void doSetStatement(PreparedStatement preparedStatement, int index, Object value) throws SQLException {
                preparedStatement.setLong(index, (Long) value);
            }
        },
        INTEGER(Integer.class) {
            @Override
            void doSetStatement(PreparedStatement preparedStatement, int index, Object value) throws SQLException {
                preparedStatement.setInt(index, (Integer) value);
            }
        };

        private static final Map<Class<?>, PreparedStatementSetEnum> MAPPING = Arrays.stream(values()).collect(Collectors.toMap(PreparedStatementSetEnum::getType, Function.identity()));

        private final Class<?> type;

        PreparedStatementSetEnum(final Class<?> type) {
            this.type = type;
        }

        public Class<?> getType() {
            return this.type;
        }

        abstract void doSetStatement(PreparedStatement statement, int index, Object value) throws SQLException;

        public static void setStatement(PreparedStatement statement, int index, Object value) throws SQLException {
            if (value == null) {
                throw new NullPointerException("value cannot be null");
            }
            PreparedStatementSetEnum preparedStatementSetEnum = MAPPING.get(value.getClass());
            preparedStatementSetEnum.doSetStatement(statement, index, value);
        }

    }

    /**
     * 结果集获取枚举
     */
    enum ResultGetEnum {

        STRING(String.class) {
            @Override
            Object doGetResult(ResultSet resultSet, String columnName) throws SQLException {
                return resultSet.getString(columnName);
            }
        },
        LONG(Long.class) {
            @Override
            Object doGetResult(ResultSet resultSet, String columnName) throws SQLException {
                return resultSet.getLong(columnName);
            }
        },
        INTEGER(Integer.class) {
            @Override
            Object doGetResult(ResultSet resultSet, String columnName) throws SQLException {
                return resultSet.getInt(columnName);
            }
        };

        private static final Map<Class<?>, ResultGetEnum> MAPPING = Arrays.stream(values()).collect(Collectors.toMap(ResultGetEnum::getType, Function.identity()));

        private final Class<?> type;

        ResultGetEnum(final Class<?> type) {
            this.type = type;
        }

        public Class<?> getType() {
            return this.type;
        }

        abstract Object doGetResult(ResultSet resultSet, String columnName) throws SQLException;

        public static Object getResult(ResultSet resultSet, Class<?> fieldType, String columnName) throws SQLException {
            ResultGetEnum resultSetEnum = MAPPING.get(fieldType);
            return resultSetEnum.doGetResult(resultSet, columnName);
        }
    }

    private static String mapColumnLabel(String fieldName) {
        return fieldName;
    }

}