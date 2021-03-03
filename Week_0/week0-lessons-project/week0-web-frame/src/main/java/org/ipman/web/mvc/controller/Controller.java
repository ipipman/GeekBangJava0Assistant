package org.ipman.web.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ipipman on 2021/3/3.
 *
 * @version V1.0
 *
 * 页面控制器，负责服务端页面渲染
 */
public interface Controller {

    /**
     * @param request   HTTP 请求
     * @param response  HTTP 响应
     * @return 试图地址路径（jsp的路径）
     * @throws Throwable 异常发生时处理
     */
    String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable;

}
