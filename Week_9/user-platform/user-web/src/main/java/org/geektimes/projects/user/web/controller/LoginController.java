package org.geektimes.projects.user.web.controller;

import org.geektimes.configuration.microprofile.config.RequestContextConfig;
import org.geektimes.web.mvc.controller.PageController;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.logging.Logger;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @since 2021/4/20
 */
@Path("/login")
public class LoginController implements PageController {

    private static Logger logger = Logger.getLogger(PageController.class.getName());

    @GET
    @POST
    @Override
    public String execute(RequestContextConfig requestContext) throws Throwable {
        logger.info("进入了login");
        return "login-form.jsp";
    }

}