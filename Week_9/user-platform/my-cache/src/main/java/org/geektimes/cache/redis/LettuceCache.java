package org.geektimes.cache.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.RedisCodec;
import org.geektimes.cache.AbstractCache;
import org.geektimes.cache.ExpirableEntry;
import org.geektimes.cache.redis.codec.DefaultRedisCodec;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.io.Serializable;
import java.util.Set;

public class LettuceCache<K extends Serializable, V extends Serializable> extends AbstractCache<K, V> {

    private final RedisClient redisClient;
    private final RedisCodec<K, V> redisCodec;

    public LettuceCache(CacheManager cacheManager, String cacheName,
                        Configuration<K, V> configuration, RedisClient redisClient) {
        super(cacheManager, cacheName, configuration);
        this.redisClient = redisClient;
        this.redisCodec = new DefaultRedisCodec<>(serializer);
    }

    @Override
    protected boolean containsEntry(K key) throws CacheException, ClassCastException {
        try (StatefulRedisConnection<K, V> connect = redisClient.connect(redisCodec)) {
            return connect.sync().exists(key) > 0;
        }
    }

    @Override
    protected ExpirableEntry<K, V> getEntry(K key) throws CacheException, ClassCastException {
        try (StatefulRedisConnection<K, V> connect = redisClient.connect(redisCodec)) {
            V value = connect.sync().get(key);
            return ExpirableEntry.of(key, value);
        }
    }

    @Override
    protected void putEntry(ExpirableEntry<K, V> newEntry) throws CacheException, ClassCastException {
        try (StatefulRedisConnection<K, V> connect = redisClient.connect(redisCodec)) {
            connect.sync().set(newEntry.getKey(), newEntry.getValue());
        }
    }

    @Override
    protected ExpirableEntry<K, V> removeEntry(K key) throws CacheException, ClassCastException {
        ExpirableEntry<K, V> oldEntry = getEntry(key);
        try (StatefulRedisConnection<K, V> connect = redisClient.connect(redisCodec)) {
            connect.sync().del(key);
        }
        return oldEntry;
    }

    @Override
    protected void clearEntries() throws CacheException {
        // TODO
    }

    @Override
    protected Set<K> keySet() {
        // TODO
        return null;
    }

    @Override
    protected void doClose() {
        this.redisClient.shutdown();
    }

}
