package com.bottomlord.projects.user.sql;

import com.bottomlord.functions.ThrowableFunction;
import com.bottomlord.projects.user.domain.User;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author liveForExp
 */
public class DBConnectionManager {
    @Resource(name = "jdbc/UserPlatformDB")
    private DataSource dataSource;
    @Resource(name = "bean/EntityManager")
    private EntityManager entityManager;

    public Connection getConnection() throws RuntimeException{
        return ThrowableFunction.execute(dataSource, DataSource::getConnection);
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void releaseConnection() {

    }

    public static final String DROP_USERS_TABLE_DDL_SQL = "DROP TABLE users";

    public static final String CREATE_USERS_TABLE_DDL_SQL = "CREATE TABLE users(" +
            "id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "name VARCHAR(16) NOT NULL, " +
            "password VARCHAR(64) NOT NULL, " +
            "email VARCHAR(64) NOT NULL, " +
            "phoneNumber VARCHAR(64) NOT NULL" +
            ")";

    public static final String INSERT_USER_DML_SQL = "INSERT INTO users(name,password,email,phoneNumber) VALUES " +
            "('A','******','a@gmail.com','1') , " +
            "('B','******','b@gmail.com','2') , " +
            "('C','******','c@gmail.com','3') , " +
            "('D','******','d@gmail.com','4') , " +
            "('E','******','e@gmail.com','5')";

    private static Connection getConnectionByClassLoader() throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Driver driver = DriverManager.getDriver("jdbc:derby:/db/user-platform;create=true");
        return driver.connect("jdbc:derby:d:/db/user-platform;create=true", new Properties());
    }

    private static Connection getConnectionByDriver() throws SQLException {
        String databaseUrl = "jdbc:derby:d:/db/user-platform;create=true";
        return DriverManager.getConnection(databaseUrl);
    }

    private static String getDynamicQuerySql(ResultSetMetaData metaData) throws SQLException {
        StringBuilder queryAllUsersSqlBuilder = new StringBuilder("SELECT");
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            queryAllUsersSqlBuilder.append(" ").append(metaData.getColumnLabel(i)).append(",");
        }
        // 移除最后一个 ","
        queryAllUsersSqlBuilder.deleteCharAt(queryAllUsersSqlBuilder.length() - 1);
        queryAllUsersSqlBuilder.append(" FROM ").append(metaData.getTableName(1));
        return queryAllUsersSqlBuilder.toString();
    }

    public static void main(String[] args) throws Exception {
        //getConnectionByClassLoader();
        Connection connection = getConnectionByDriver();
        Statement statement = connection.createStatement();
        statement.execute(DROP_USERS_TABLE_DDL_SQL);
        statement.execute(CREATE_USERS_TABLE_DDL_SQL);
        statement.executeUpdate(INSERT_USER_DML_SQL);
        ResultSet resultSet = statement.executeQuery("SELECT id,name,password,email,phoneNumber FROM users");
        BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
        //通过元数据动态生成查询sql
        System.out.println(getDynamicQuerySql(resultSet.getMetaData()));

        while (resultSet.next()) {
            User user = new User();

            for (PropertyDescriptor propertyDescriptor : userBeanInfo.getPropertyDescriptors()) {
                String fieldName = propertyDescriptor.getName();
                Class fieldType = propertyDescriptor.getPropertyType();
                String methodName = typeMethodMappings.get(fieldType);
                String columnLabel = mapColumnLabel(fieldName);
                Method resultSetMethod = ResultSet.class.getMethod(methodName, String.class);
                Object resultValue = resultSetMethod.invoke(resultSet, columnLabel);
                Method setterMethodFromUser = propertyDescriptor.getWriteMethod();
                setterMethodFromUser.invoke(user, resultValue);
            }

            System.out.println(user);
        }


        connection.close();
    }

    private static String mapColumnLabel(String fieldName) {
        return fieldName;
    }

    /**
     * 数据类型与 ResultSet 方法名映射
     */
    static Map<Class, String> typeMethodMappings = new HashMap<>();

    static {
        typeMethodMappings.put(Long.class, "getLong");
        typeMethodMappings.put(String.class, "getString");
    }
}
