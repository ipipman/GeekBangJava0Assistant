package com.bottomlord.web.mvc;

import java.lang.reflect.Method;
import java.util.Set;

public class HandlerMethodInfo {
    private final String requestPath;

    private final Method handlerMethod;

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
