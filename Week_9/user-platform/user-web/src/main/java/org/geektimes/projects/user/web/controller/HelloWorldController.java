package org.geektimes.projects.user.web.controller;

import org.geektimes.configuration.microprofile.config.RequestContextConfig;
import org.geektimes.web.mvc.controller.PageController;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * 输出 “Hello,World” Controller
 */
@Path("/hello")
public class HelloWorldController implements PageController {

//    @GET
//    @POST
//    @Path("/world") // /hello/world -> HelloWorldController
//    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
//        return "index.jsp";
//    }

    @GET
    @POST
    @Override
    public String execute(RequestContextConfig requestContext) throws Throwable {
        return "index.jsp";
    }

}
