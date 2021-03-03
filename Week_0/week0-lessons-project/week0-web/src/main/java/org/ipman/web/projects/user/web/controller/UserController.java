package org.ipman.web.projects.user.web.controller;

import org.ipman.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

    @POST
    @GET
    @Path("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        return "/login-form.jsp";
    }

}
