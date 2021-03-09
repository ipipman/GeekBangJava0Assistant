package org.ipman.web.projects.user.sql;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import java.sql.*;

/**
 * Created by ipipman on 2021/3/3.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.sql
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/3 4:39 下午
 */
public class DBConnectionManager { // JNDI Commpont

    private Connection connection;

    public DBConnectionManager() {
        try {
            // TODO: Testing JNDI
            Context initContext = new InitialContext();
            DataSource dataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/UserPlatformDB");
            connection = dataSource.getConnection();
        } catch (Throwable e) {
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

}
