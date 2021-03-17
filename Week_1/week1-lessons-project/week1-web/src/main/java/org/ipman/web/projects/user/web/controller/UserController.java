package org.ipman.web.projects.user.web.controller;

import org.ipman.web.context.ComponentContext;
import org.ipman.web.mvc.controller.PageController;
import org.ipman.web.projects.user.domain.User;
import org.ipman.web.projects.user.service.UserServiceImpl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;


/**
 * Created by ipipman on 2021/3/3.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.web.controller
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/3 11:45 上午
 */
@Path("/user")
public class UserController implements PageController {

    @GET
    @Path("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        User user = new User();
        user.setName("ipsd");
        user.setPassword("***");
        user.setEmail("ipman@163.com");
        user.setPhoneNumber("15810833333");

        // 从 JNDI 容器中，获取 Component Context
        ComponentContext componentContext = ComponentContext.getInstance();
        UserServiceImpl userService = componentContext.getComponent("bean/UserService");

        // Hibernate persist 持久化
        userService.register(user);

        return "/login-form.jsp";
    }

}
