package org.geektimes.web.servlet.listener;

import org.geektimes.configuration.microprofile.config.RequestContextConfig;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * <p>
 * 请求上下文监听
 * <p>
 *
 * @author liuwei
 * @since 2021/3/25
 */
public class RequestContextListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent sre) {

    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        RequestContextConfig.destroy();
        sre.getServletContext().log("线程[" + Thread.currentThread().getName() + "] RequestContext已销毁");
    }

}