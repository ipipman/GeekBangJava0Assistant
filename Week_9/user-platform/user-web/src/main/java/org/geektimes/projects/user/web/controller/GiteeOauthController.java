package org.geektimes.projects.user.web.controller;

import org.geektimes.configuration.microprofile.config.RequestContextConfig;
import org.geektimes.projects.user.service.GiteeOauthService;
import org.geektimes.web.mvc.controller.PageController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * <p>
 * 认证回调
 * <p>
 *
 * @author liuwei
 * @since 2021/4/20
 */
@Path("/gitee/oauth")
public class GiteeOauthController implements PageController {

    private static Logger logger = Logger.getLogger(GiteeOauthController.class.getName());

    @Resource(name = "bean/GiteeOauthService")
    private GiteeOauthService giteeOauthService;

    @GET
    @Override
    public String execute(RequestContextConfig requestContext) throws Throwable {
        String requestType = requestContext.getValue("type");
        if (Objects.equals(requestType, "authorize")) {
            String authorizeUrl = giteeOauthService.getAuthorizeUrl();
            return "redirect:" + authorizeUrl;
        }


        if (Objects.equals(requestType, "callback")) {
            String code = requestContext.getValue("code");
            logger.info("进入了authCallBack，code=" + code);

            String accessToken = giteeOauthService.getAccessToken(code);
            logger.info("获取到了accessToken：" + accessToken);

            Map<String, Object> userInfo = giteeOauthService.getUserInfo(accessToken);
            logger.info("获取到了用户信息：" + userInfo);

            HttpServletRequest request = requestContext.getRequest();
            request.setAttribute("userInfo", userInfo.toString());
            return "index.jsp";
        }

        return "index.jsp";
    }

}