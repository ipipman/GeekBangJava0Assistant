package org.geektimes.cache.redis.codec;

import io.lettuce.core.codec.RedisCodec;
import org.geektimes.cache.serializer.CacheSerializer;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @since 2021/4/14
 */
public class DefaultRedisCodec<K extends Serializable, V extends Serializable> implements RedisCodec<K, V> {

    final CacheSerializer serializer;

    public DefaultRedisCodec() {
        this.serializer = CacheSerializer.getInstance();
    }

    public DefaultRedisCodec(CacheSerializer cacheSerializer) {
        this.serializer = cacheSerializer;
    }

    @Override
    public K decodeKey(ByteBuffer byteBuffer) {
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes, 0, bytes.length);
        return serializer.deserialize(bytes);
    }

    @Override
    public V decodeValue(ByteBuffer byteBuffer) {
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes, 0, bytes.length);
        return serializer.deserialize(bytes);
    }

    @Override
    public ByteBuffer encodeKey(K key) {
        return ByteBuffer.wrap(serializer.serialize(key));
    }

    @Override
    public ByteBuffer encodeValue(V value) {
        return ByteBuffer.wrap(serializer.serialize(value));
    }

}