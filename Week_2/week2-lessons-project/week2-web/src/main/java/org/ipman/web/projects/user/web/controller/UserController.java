package org.ipman.web.projects.user.web.controller;

import org.eclipse.microprofile.config.Config;
import org.ipman.web.configuration.microprofile.config.DefaultConfigProviderResolver;
import org.ipman.web.configuration.microprofile.config.JavaConfig;
import org.ipman.web.context.ComponentContext;
import org.ipman.web.mvc.controller.PageController;
import org.ipman.web.projects.user.domain.User;
import org.ipman.web.projects.user.service.UserServiceImpl;
import org.ipman.web.projects.user.validator.bean.validation.DelegatingValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Set;


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

        // 获取 Eclipse MicroProfile Config Source
        DefaultConfigProviderResolver configProviderResolver = componentContext.getComponent("bean/DefaultConfigProviderResolver");
        Config config = configProviderResolver.getConfig();
        System.out.println(config.getValue("user.home", String.class));

        // 获取 委派 Validator
        DelegatingValidator delegatingValidator = componentContext.getComponent("bean/DelegatingValidator");
        Validator validator = delegatingValidator.getValidator();

        // 校验结果
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(c -> {
            System.out.println(c.getMessage());
        });

        UserServiceImpl userService = componentContext.getComponent("bean/UserService");

        // Hibernate persist 持久化
        userService.register(user);

        return "/login-form.jsp";
    }

}
