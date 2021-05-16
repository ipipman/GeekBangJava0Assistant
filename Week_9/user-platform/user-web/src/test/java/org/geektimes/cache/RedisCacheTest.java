package org.geektimes.cache;

import org.geektimes.projects.user.cache.RedisCacheManager;
import org.geektimes.projects.user.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.cache.Cache;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @date 2021/5/12
 */
public class RedisCacheTest {

    RedisCacheManager redisCacheManager = new RedisCacheManager("redis://127.0.0.1:6379");

    @Test
    public void test() {
        Cache userCache = redisCacheManager.getCache("user");

        // 设置缓存
        User zs = new User("张三", "******", "xxx@gmail.com", "1878888888");
        User ls = new User("李四", "******", "xxx@gmail.com", "1878888888");
        userCache.put("zs", zs);
        userCache.put("ls", ls);

        // 获取缓存
        zs = userCache.get("zs", User.class);
        ls = userCache.get("ls", User.class);
        Assert.assertNotNull(zs);
        Assert.assertNotNull(ls);
        System.out.println("张三信息：" +  zs);
        System.out.println("李四信息：" +  ls);

        // 清空缓存
        userCache.invalidate();
        Assert.assertNull(userCache.get("zs"));
        Assert.assertNull(userCache.get("ls"));
    }


}