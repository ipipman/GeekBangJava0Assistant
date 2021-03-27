package com.bottomlord.web.mvc;

import com.bottomlord.web.mvc.controller.Controller;
import com.bottomlord.web.mvc.controller.PageController;
import org.apache.commons.lang.StringUtils;

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

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.substringAfter;

/**
 * @author ChenYue
 * @date 2021/3/2 18:07
 */
public class FrontControllerServlet extends HttpServlet {
    private static final String SLANT = "/";
    private final Map<String, Controller> controllerMapping;
    private final Map<String, HandlerMethodInfo> handlerMethodInfoMapping;

    public FrontControllerServlet() {
        this.controllerMapping = new HashMap<>();
        this.handlerMethodInfoMapping = new HashMap<>();
    }

    @Override
    public void init() throws ServletException {
        initHandlerMethodInfoMapping();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        String prefixPath = req.getContextPath();
        String requestMappingPath = substringAfter(requestUri, StringUtils.replace(prefixPath, "//", "/"));

        Controller controller = controllerMapping.get(requestMappingPath);

        if (controller != null) {
            HandlerMethodInfo handlerMethodInfo = handlerMethodInfoMapping.get(requestMappingPath);

            try {
                if (handlerMethodInfo != null) {
                    String httpMethod = req.getMethod();

                    if (!handlerMethodInfo.getSupportedHttpMethods().contains(httpMethod)) {
                        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        return;
                    }

                    if (controller instanceof PageController) {
                        PageController pageController = (PageController) controller;
                        String viewPath = pageController.execute(req, resp);
                        ServletContext servletContext = req.getServletContext();

                        if (!viewPath.startsWith(SLANT)) {
                            viewPath = SLANT + viewPath;
                        }

                        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPath);
                        requestDispatcher.forward(req, resp);
                    }
                }
            } catch (Throwable throwable) {
                if (throwable.getCause() instanceof IOException) {
                    throw (IOException) throwable.getCause();
                } else {
                    throw new ServletException(throwable);
                }
            }
        }
    }

    private void initHandlerMethodInfoMapping() {
        for (Controller controller : ServiceLoader.load(Controller.class)) {
            Class<?> controllerClass = controller.getClass();
            Path pathFromClass = controllerClass.getAnnotation(Path.class);
            String requestPath = pathFromClass.value();
            Method[] publicMethods = controllerClass.getMethods();
            for (Method method : publicMethods) {
                Set<String> supportedHttpMethods = findSupportedHttpMethods(method);
                Path pathFromMethod = method.getAnnotation(Path.class);
                if (pathFromMethod != null) {
                    requestPath += pathFromMethod.value();
                }
                handlerMethodInfoMapping.put(requestPath, new HandlerMethodInfo(requestPath, method, supportedHttpMethods));
            }
            controllerMapping.put(requestPath, controller);
        }
    }

    private Set<String> findSupportedHttpMethods(Method method) {
        Set<String> supportedHttpMethods = new LinkedHashSet<>();
        for (Annotation annotation : method.getAnnotations()) {
            HttpMethod httpMethod = annotation.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                supportedHttpMethods.add(httpMethod.value());
            }
        }

        if (supportedHttpMethods.isEmpty()) {
            supportedHttpMethods.addAll(asList(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS));
        }

        return supportedHttpMethods;
    }
}
