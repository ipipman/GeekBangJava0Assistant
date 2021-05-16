package org.geektimes.cache.serializer.exceptiion;

/**
 * <p>
 * 序列化异常
 * <p>
 *
 * @author liuwei
 * @since 2021/4/14
 */
public class SerializationException extends RuntimeException {

	public SerializationException(String message) {
		super(message);
	}

	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}

}