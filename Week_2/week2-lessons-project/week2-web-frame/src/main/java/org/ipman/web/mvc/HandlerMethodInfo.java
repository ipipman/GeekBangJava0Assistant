package org.ipman.web.mvc;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by ipipman on 2021/3/3.
 *
 * 处理方法信息类
 */
public class HandlerMethodInfo {

    // Path 请求路径
    private final String requestPath;

    // Method 处理方法
    private final Method handlerMethod;

    // 所有支持的 Method 类型
    private final Set<String> supportedHttpMethods;

    public HandlerMethodInfo(String requestPath, Method handlerMethod, Set<String> supportedHttpMethods) {
        this.requestPath = requestPath;
        this.handlerMethod = handlerMethod;
        this.supportedHttpMethods = supportedHttpMethods;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public Set<String> getSupportedHttpMethods() {
        return supportedHttpMethods;
    }
}
