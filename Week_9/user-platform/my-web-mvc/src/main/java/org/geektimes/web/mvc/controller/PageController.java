package org.geektimes.web.mvc.controller;

import org.geektimes.configuration.microprofile.config.RequestContextConfig;

/**
 * 页面控制器，负责服务端页面渲染
 *
 * @since 1.0
 */
public interface PageController extends Controller {

    /**
     * @param requestContext 请求上下文
     * @return 视图地址路径
     * @throws Throwable 异常发生时
     */
    String execute(RequestContextConfig requestContext) throws Throwable;

}
