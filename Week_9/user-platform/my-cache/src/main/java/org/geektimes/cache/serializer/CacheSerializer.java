package org.geektimes.cache.serializer;

import java.io.Serializable;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * <p>
 * 缓存序列化
 * <p>
 *
 * @author liuwei
 * @since 2021/4/14
 */
public abstract class CacheSerializer {

    private static volatile CacheSerializer INSTANCE;

    /**
     * 序列化
     *
     * @param source 序列对象
     * @return 序列字节
     */
    public final byte[] serialize(Serializable source) {
        if (source == null) {
            return new byte[0];
        }
        return doSerialize(source);
    }

    /**
     * 序列化
     *
     * @param source 序列对象
     * @return 序列字节
     */
    public abstract byte[] doSerialize(Serializable source);

    /**
     * 反序列化
     *
     * @param data 序列字节
     * @return 序列对象
     */
    public final <T> T deserialize(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        return doDeserialize(data);
    }

    /**
     * 反序列化
     *
     * @param data 序列字节
     * @return 序列对象
     */
    public abstract <T> T doDeserialize(byte[] data);

    public static CacheSerializer getInstance() {
        if (INSTANCE == null) {
            synchronized (CacheSerializer.class) {
                if (INSTANCE != null) {
                    return INSTANCE;
                }
                INSTANCE = loadSpi(CacheSerializer.class.getClassLoader());
            }
        }
        return INSTANCE;
    }

    private static CacheSerializer loadSpi(ClassLoader classLoader) {
        ServiceLoader<CacheSerializer> serviceLoader = ServiceLoader.load(CacheSerializer.class, classLoader);
        Iterator<CacheSerializer> iterator = serviceLoader.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return new DefaultCacheSerializer();
    }

}