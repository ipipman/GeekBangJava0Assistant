package org.geektimes.week7.oauth2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by ipipman on 2021/4/23.
 *
 * @version V1.0
 * @Package org.geektimes.week7.oauth2.config
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/4/23 4:55 下午
 */
@Data
@Component
@ConfigurationProperties(prefix = "github")
public class Oauth2Properties {
    private String clientId;
    private String clientSecret;
    private String authorizeUrl;
    private String redirectUrl;
    private String accessTokenUrl;
    private String userInfoUrl;
}
