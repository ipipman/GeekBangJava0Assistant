package com.bottomlord.web.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ChenYue
 * @date 2021/3/2 18:11
 */
public interface PageController extends Controller {

    /**
     * forward
     * @param request  HTTP 请求
     * @param response HTTP 相应
     * @return 视图地址路径
     * @throws Throwable 异常发生时
     */
    String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable;
}
