package org.geektimes.projects.user.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @since 2021/4/21
 */
public class GiteeOauthServiceImpl implements GiteeOauthService {

    @ConfigProperty(name = "gitee.oauth.client-id")
    private String clientId;

    @ConfigProperty(name = "gitee.oauth.client-secret")
    private String clientSecret;

    private static final String AUTHORIZE_URL = "https://gitee.com/oauth/authorize";
    private static final String TOKEN_URL = "https://gitee.com/oauth/token";
    private static final String USER_URL = "https://gitee.com/api/v5/user";
    private static final String REDIRECT_URI = "http://localhost:8080/gitee/oauth?type=callback";

    @Override
    public String getAuthorizeUrl() {
        return AUTHORIZE_URL + "?response_type=code&redirect_uri=" + REDIRECT_URI + "&client_id=" + clientId;
    }

    @Override
    public String getAccessToken(String code) {
        Client client = ClientBuilder.newClient();
        HashMap<Object, Object> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        params.put("client_id", clientId);
        params.put("redirect_uri", REDIRECT_URI);
        params.put("client_secret", clientSecret);
        Entity<Object> entity = Entity.entity(params, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        Response response = client
                .target(TOKEN_URL)
                .request()
                .post(entity);
        Map result = response.readEntity(Map.class);
        return result.get("access_token").toString();
    }

    @Override
    public Map<String, Object> getUserInfo(String accessToken) {
        String userApi = USER_URL + "?access_token=" + accessToken;
        Client client = ClientBuilder.newClient();
        Response response = client
                .target(userApi)
                .request()
                .get();
        Map map = response.readEntity(Map.class);
        return map;
    }

}