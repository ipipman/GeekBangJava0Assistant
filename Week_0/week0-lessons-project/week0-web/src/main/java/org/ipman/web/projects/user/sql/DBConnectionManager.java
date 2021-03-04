package org.ipman.web.projects.user.sql;

import org.ipman.web.projects.user.domain.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ipipman on 2021/3/3.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.sql
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/3 4:39 下午
 */
public class DBConnectionManager {

    private Connection connection;

    public DBConnectionManager() {
        try {
//        // TODO: Testing JNDI
//        Context initContext = new InitialContext();
//        DataSource dataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/UserPlatformDB");
            String databaseURL = "jdbc:derby:/Users/huangyan110110114/GeekBangJava0Assistant/Week_0/week0-lessons-project/week0-db/user-platform;create=true";
            Connection connection = DriverManager.getConnection(databaseURL);
            this.connection = connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String mapColumnLabel(String fieldName) {
        return fieldName;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void releaseConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * 数据类型与 ResultSet 方法名映射
     */
    static Map<Class, String> typeMethodMappings = new HashMap<>();

    static {
        typeMethodMappings.put(Long.class, "getLong");
        typeMethodMappings.put(String.class, "getString");
    }

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

    public static void main(String[] args) throws Exception {
        String databaseURL = "jdbc:derby:Week_0/week0-lessons-project/week0-db/user-platform;create=true";
        Connection connection = DriverManager.getConnection(databaseURL);
        Statement statement = connection.createStatement();
        // 创建 users 表
        System.out.println(statement.execute(CREATE_USERS_TABLE_DDL_SQL)); // false
        System.out.println(statement.executeUpdate(INSERT_USER_DML_SQL));  // 5

    }
}
