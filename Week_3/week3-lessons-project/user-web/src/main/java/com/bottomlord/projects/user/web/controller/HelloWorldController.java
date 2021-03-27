package com.bottomlord.projects.user.web.controller;

import com.bottomlord.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author ChenYue
 * @date 2021/3/2 22:35
 */
@Path("/hello")
public class HelloWorldController implements PageController {
    @Override
    @Path("/world")
    @GET
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        return "index.jsp";
    }
}
