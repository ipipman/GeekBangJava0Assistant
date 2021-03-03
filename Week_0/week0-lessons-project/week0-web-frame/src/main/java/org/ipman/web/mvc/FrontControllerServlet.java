package org.ipman.web.mvc;

import org.ipman.web.mvc.controller.Controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by ipipman on 2021/3/3.
 *
 * @version V1.0
 * @Package org.ipman.web.mvc
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/3 11:05 上午
 */

public class FrontControllerServlet extends HttpServlet {

    // Path 与 Method 的映射关系
    private Map<String, HandlerMethodInfo> handlerMethodInfoMapping = new HashMap<>();

    /**
     * 第一步：初始化 Servlet
     */
    @Override
    public void init() throws ServletException {
        super.init();
        initHandleMethods();
    }

    /**
     * 第一步：初始化 Servlet
     * 读取所有的 RestController 的注解元信息 @Path
     * 利用 ServiceLoader 技术 （Java SPI）
     */
    private void initHandleMethods() {
        // 利用 SPI 获取所有注册 Controller
        for (Controller controller : ServiceLoader.load(Controller.class)) {
            Class<?> controllerClass = controller.getClass();
            // 获取所有标记 Controller 的 Path
            Path pathFromClass = controllerClass.getAnnotation(Path.class);
            String requestPath = pathFromClass.value();
            Method[] publicMethods = controllerClass.getMethods();

            // 处理方法支持的 HTTP 方法集合
            for (Method method : publicMethods) {
                Set<String> supportedHttpMethods = findSupportedHttpMethods(method);
                Path pathFromMethod = method.getAnnotation(Path.class);
                if (pathFromMethod != null){
                    requestPath += pathFromMethod.value();
                }
                // 记录 Path 与 Method 的映射关系
                handlerMethodInfoMapping.put(requestPath,
                        new HandlerMethodInfo(requestPath, method, supportedHttpMethods));
            }
        }
    }

    /**
     * 第一步：初始化 Servlet
     * 获取处理方法中的标注的 HTTP 方法集合
     */
    private Set<String> findSupportedHttpMethods(Method method) {
        Set<String> supportedHttpMethods = new LinkedHashSet<>();
        for (Annotation annotationFromMethod : method.getAnnotations()) {
            // 获取所有方法的标注
            HttpMethod httpMethod = annotationFromMethod.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                supportedHttpMethods.add(httpMethod.value());
            }
        }
        if (supportedHttpMethods.isEmpty()) {
            supportedHttpMethods.addAll(Arrays.asList(HttpMethod.GET, HttpMethod.POST,
                    HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS));
        }
        return supportedHttpMethods;
    }



    /**
     * 第二步：Service 路由
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext servletContext = request.getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/user.jsp");
        requestDispatcher.forward(request, response);
    }


}
