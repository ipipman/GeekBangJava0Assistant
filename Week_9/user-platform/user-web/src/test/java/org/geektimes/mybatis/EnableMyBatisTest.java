package org.geektimes.mybatis;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.mybatis.annotation.EnableMyBatis;
import org.geektimes.projects.user.repository.mapper.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @date 2021/5/12
 */
@Configuration
@EnableMyBatis(dataSource = "dataSource",
        configLocation = "/META-INF/mybatis/mybatis-config.xml",
        mapperLocations = {"classpath*:META-INF/mappers/**/*.xml"},
        configurationProperties = "/META-INF/jdbc.properties",
        typeAliasesPackage = "org.geektimes.projects.user.domain",
        environment = "development")
@MapperScan("org.geektimes.**.mapper")
public class EnableMyBatisTest {

    @Test
    public void test() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(EnableMyBatisTest.class);
        context.refresh();

        UserMapper userMapper = context.getBean(UserMapper.class);
        // 创建表结构
        try {
            userMapper.dropTable();
        } catch (Exception ignored) { }
        userMapper.createTable();

        // 插入用户
        User user = new User("张三", "******", "xxx@gmail.com", "1878888888");
        Assert.assertTrue(userMapper.insert(user));
        System.out.println("插入用户信息成功");

        // 查询用户
        Long userId = user.getId();
        user = userMapper.selectById(userId);
        Assert.assertNotNull(user);
        System.out.println("获取用户信息：" + user);

        // 删除用户信息
        Assert.assertTrue(userMapper.deleteById(userId));
        System.out.println("删除用户信息成功");

        context.close();
    }

    @Bean
    public DataSource dataSource() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        dataSource.setUrl("jdbc:derby:db/user-platform;create=true");
        dataSource.setName("");
        dataSource.setPassword("");
        return dataSource;
    }
    

}