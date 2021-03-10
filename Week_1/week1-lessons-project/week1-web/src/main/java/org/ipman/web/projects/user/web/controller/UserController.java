package org.ipman.web.projects.user.web.controller;

import org.ipman.web.mvc.controller.PageController;
import org.ipman.web.projects.user.domain.User;
import org.ipman.web.projects.user.repository.DatabaseUserRepository;
import org.ipman.web.projects.user.repository.UserRepository;
import org.ipman.web.projects.user.sql.DBConnectionManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.Suspended;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

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

    private DatabaseUserRepository databaseUserRepository = new DatabaseUserRepository();

    @POST
    @GET
    @Path("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        User user = new User();
        user.setName("ipmana");
        user.setPassword("***");
        user.setEmail("ipman@163.com");
        user.setPhoneNumber("15810833333");
        databaseUserRepository.save(user);
        Collection<User> users = databaseUserRepository.getAll();
        return "/login-form.jsp";
    }

}
