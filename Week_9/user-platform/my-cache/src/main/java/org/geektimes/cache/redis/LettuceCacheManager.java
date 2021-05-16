package org.geektimes.cache.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.geektimes.cache.AbstractCacheManager;

import javax.cache.Cache;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

/**
 * {@link javax.cache.CacheManager} based on Jedis
 */
public class LettuceCacheManager extends AbstractCacheManager {

    private final RedisURI redisURI;
    private final ClientResources resources;

    public LettuceCacheManager(CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
        super(cachingProvider, uri, classLoader, properties);
        RedisURI redisURI = RedisURI.create(uri);
        redisURI.setTimeout(Duration.of(5, ChronoUnit.SECONDS));
        this.redisURI = redisURI;
        this.resources = DefaultClientResources.create();
    }

    @Override
    protected <K, V, C extends Configuration<K, V>> Cache doCreateCache(String cacheName, C configuration) {
        RedisClient client = RedisClient.create(resources, redisURI);
        return new LettuceCache(this, cacheName, configuration, client);
    }

    @Override
    protected void doClose() {
        resources.shutdown();
    }
}
