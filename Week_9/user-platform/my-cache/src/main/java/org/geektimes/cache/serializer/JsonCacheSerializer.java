package org.geektimes.cache.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.geektimes.cache.serializer.exceptiion.SerializationException;

import java.io.Serializable;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @since 2021/4/14
 */
public class JsonCacheSerializer extends CacheSerializer {

    private final ObjectMapper objectMapper;

    public JsonCacheSerializer() {
        this.objectMapper = initObjectMapper(new ObjectMapper());
    }

    @Override
    public byte[] doSerialize(Serializable source) {
        try {
            return this.objectMapper.writeValueAsBytes(source);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    @Override
    public <T> T doDeserialize(byte[] data) {
        try {
            return (T) objectMapper.readValue(data, Object.class);
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    public ObjectMapper initObjectMapper(ObjectMapper objectMapper) {
        // 禁用将日期写入时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 不显示为null的字段
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 过滤对象的null属性.
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 忽略transient
        objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
        // 启用类型信息自动包含的方法（序列化的json将包含对象元信息）
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        return objectMapper;
    }

}