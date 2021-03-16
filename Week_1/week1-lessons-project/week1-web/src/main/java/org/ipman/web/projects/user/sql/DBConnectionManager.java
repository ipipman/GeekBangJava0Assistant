package org.ipman.web.projects.user.sql;


import javax.annotation.Resource;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ipipman on 2021/3/3.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.sql
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/3 4:39 下午
 */
public class DBConnectionManager { // JNDI Commpont

    private final static Logger logger = Logger.getLogger(DBConnectionManager.class.getName());

    @Resource(name = "jdbc/UserPlatformDB")
    private DataSource dataSource;

    @Resource(name = "bean/EntityManager")
    private EntityManager entityManager;

    private Connection connection;

    /*
     * 获取 JPA EntityManager
     */
    public EntityManager getEntityManager() {
        logger.log(Level.INFO, "当前 EntityManager 实现类：" + entityManager.getClass().getName());
        return entityManager;
    }

    /*
     * 获取 JNDI 数据库连接
     */
    public Connection getConnection() {
        // 依赖查找
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        if (connection != null) {
            logger.log(Level.INFO, "获取 JNDI 数据库连接成功");
        }
        return connection;
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
