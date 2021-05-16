package org.geektimes.projects.user.exception;

/**
 * <p>
 * 用户注册异常
 * <p>
 *
 * @author liuwei
 * @since 2021/3/9
 */
public class UserRegisterException extends RuntimeException {

    public UserRegisterException(String message) {
        super(message);
    }

    public UserRegisterException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserRegisterException(Throwable cause) {
        super(cause);
    }

    public UserRegisterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}