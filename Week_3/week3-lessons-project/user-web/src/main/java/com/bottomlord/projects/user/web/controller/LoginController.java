package com.bottomlord.projects.user.web.controller;

import com.bottomlord.configuration.microprofile.config.source.servlet.ConfigHolder;
import com.bottomlord.context.ClassicComponentContext;
import com.bottomlord.projects.user.domain.User;
import com.bottomlord.projects.user.service.UserService;
import com.bottomlord.web.mvc.controller.PageController;
import org.eclipse.microprofile.config.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;

/**
 * @author ChenYue
 * @date 2021/3/2 22:36
 */
@Path("/register")
public class LoginController implements PageController {
    private final UserService userService;

    public LoginController() {
        this.userService = ClassicComponentContext.getInstance().getComponent("bean/UserService");
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        if (HttpMethod.GET.equals(request.getMethod())) {
            return "login-form.jsp";
        } else if (HttpMethod.POST.equals(request.getMethod())) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            User user = new User();
            user.setEmail(email);
            user.setName(email.substring(0, email.indexOf('@')));
            user.setPassword(password);
            Config config = (Config) request.getServletContext().getAttribute("servletContextConfig");
            String applicationName = config.getValue("application.name", String.class);;
            user.setEmail(user.getName() + "@" + applicationName + ".com");
            System.out.println(config == ConfigHolder.get());
            userService.register(user);

            User result = userService.queryUserByNameAndPassword(user.getName(), user.getPassword());
            request.setAttribute("name", result == null ? "查无此人" : result.toString());
            request.setAttribute("applicationName", applicationName);
            return "dashboard.jsp";
        }
        return "index.jsp";
    }
}
