package org.ipman.web.projects.user.sql;

import org.ipman.web.projects.user.domain.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
            String databaseURL = "jdbc:derby:~/GeekBangJava0Assistant/Week_0/week0-lessons-project/week0-db/user-platform;create=true";
            Connection connection = DriverManager.getConnection(databaseURL);
            this.connection = connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
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

}
