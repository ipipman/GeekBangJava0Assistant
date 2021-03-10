package org.ipman.web.projects.user.sql;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by ipipman on 2021/3/10.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.sql
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/10 11:14 上午
 */
public class InitDerbyMetaData {

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

    public static void main(String[] args) {
        try {
            String databaseURL = "jdbc:derby:Week_1/week1-lessons-project/week1-db/user-platform;create=true";
            Connection connection = DriverManager.getConnection(databaseURL);
            Statement statement = connection.createStatement();

            statement.execute(CREATE_USERS_TABLE_DDL_SQL);
            statement.executeUpdate(INSERT_USER_DML_SQL);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

}
