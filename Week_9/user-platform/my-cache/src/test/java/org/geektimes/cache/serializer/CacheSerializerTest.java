package org.geektimes.cache.serializer;

import org.geektimes.cache.redis.codec.DefaultRedisCodec;
import org.geektimes.cache.serializer.model.User;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @since 2021/4/14
 */
public class CacheSerializerTest {

    @Test
    public void testInstance() {
        CacheSerializer serializer = CacheSerializer.getInstance();
        byte[] bytes = serializer.serialize(new User(2L, "李四", 18));
        User user = serializer.deserialize(bytes);
        assertNotNull(user);
        System.out.println(user);
    }

    @Test
    public void defaultSerialize() {
        CacheSerializer serializer = new DefaultCacheSerializer();
        byte[] bytes = serializer.serialize(new User(1L, "张三", 18));
        User user = serializer.deserialize(bytes);
        assertNotNull(user);
        System.out.println(user);
    }

    @Test
    public void jsonSerialize() {
        CacheSerializer serializer = new JsonCacheSerializer();
        byte[] bytes = serializer.serialize(new User(2L, "李四", 18));
        User user = serializer.deserialize(bytes);
        assertNotNull(user);
        System.out.println(user);
    }

    @Test
    public void redisCodec() {
        CacheSerializer jsonCacheSerializer = new JsonCacheSerializer();
        // CacheSerializer defaultCacheSerializer = new DefaultCacheSerializer();
        DefaultRedisCodec<String, User> redisCodec = new DefaultRedisCodec<>(jsonCacheSerializer);
        ByteBuffer keyByteBuffer = redisCodec.encodeKey("123");
        ByteBuffer valueByteBuffer = redisCodec.encodeValue(new User(2L, "李四", 18));
        String key = redisCodec.decodeKey(keyByteBuffer);
        User value = redisCodec.decodeValue(valueByteBuffer);
        assertNotNull(key);
        assertNotNull(value);
        System.out.println(key);
        System.out.println(value);
    }

}