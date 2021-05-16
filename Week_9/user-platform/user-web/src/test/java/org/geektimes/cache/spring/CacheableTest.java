package org.geektimes.cache.spring;

import org.geektimes.projects.user.cache.RedisCacheManager;
import org.geektimes.projects.user.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @date 2021/5/12
 */
@EnableCaching
@Configuration
public class CacheableTest {

    @Test
    public void test() throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(CacheableTest.class);
        context.refresh();

        CacheableTest cacheableTest = context.getBean(CacheableTest.class);
        // 缓存中没有，添加到缓存里面去
        User zs = cacheableTest.get("zs");
        // 从缓存中获取
        zs = cacheableTest.get("zs");
        Assert.assertNotNull(zs);
        System.out.println("获取到缓存：" + zs);
        // 清空缓存
        cacheableTest.clear();

        CacheManager cacheManager = cacheableTest.cacheManager();
        Assert.assertNull(cacheManager.getCache("users").get("zs"));
        Assert.assertNull(cacheManager.getCache("tests").get("zs"));
        context.close();
    }

    @Bean
    public CacheManager cacheManager() {
        return new RedisCacheManager("redis://127.0.0.1:6379");
    }

    @Cacheable(value={"users", "tests"}, key="#name")
    public User get(String name) {
        System.out.println("设置缓存：" + name);
        return new User(name, "******", "xxx@gmail.com", "1878888888");
    }

    @CacheEvict(value={"users", "tests"}, key="#name")
    public void evict(String name) {
        System.out.println("删除缓存：" + name);
    }

    @CacheEvict(value={"users", "tests"}, allEntries=true)
    public void clear() {
        System.out.println("清空缓存");
    }

}