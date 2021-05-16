package org.geektimes.cache.redis;

import org.geektimes.cache.event.TestCacheEntryListener;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.net.URI;

import static org.geektimes.cache.configuration.ConfigurationUtils.cacheEntryListenerConfiguration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @since 2021/4/14
 */
public class LettuceCacheManagerTest {

    @Test
    public void test() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager(URI.create("redis://192.168.1.202:6379/"), null);
        // configure the cache
        MutableConfiguration<String, Integer> config =
                new MutableConfiguration<String, Integer>()
                        .setTypes(String.class, Integer.class);

        // create the cache
        Cache<String, Integer> cache = cacheManager.createCache("redisCache", config);

        // add listener
        cache.registerCacheEntryListener(cacheEntryListenerConfiguration(new TestCacheEntryListener<>()));

        // cache operations
        String key = "redis-key";
        Integer value1 = 1;
        cache.put(key, value1);

        // update
        value1 = 2;
        cache.put(key, value1);

        Integer value2 = cache.get(key);
        assertEquals(value1, value2);
        cache.remove(key);
        assertNull(cache.get(key));
    }
    

}