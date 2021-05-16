package org.geektimes.projects.user.service;

import java.util.Map;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @since 2021/4/21
 */
public interface GiteeOauthService {

    /**
     * 获取认证地址
     *
     * @return 认证地址
     */
    String getAuthorizeUrl();

    /**
     * 获取访问token
     *
     * @return 访问token
     */
    String getAccessToken(String code);

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    Map<String, Object> getUserInfo(String accessToken);

}