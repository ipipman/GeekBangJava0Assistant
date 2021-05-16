package org.geektimes.web.servlet.filter;

import org.geektimes.configuration.microprofile.config.RequestContextConfig;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @since 2021/3/24
 */
public class RequestContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        req.getServletContext().log("线程[" + Thread.currentThread().getName() + "] RequestContext构建成功");
        RequestContextConfig.build(req, res);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        RequestContextConfig.destroy();
    }

}