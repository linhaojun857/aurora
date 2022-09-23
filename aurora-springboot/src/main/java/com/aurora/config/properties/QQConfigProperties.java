package com.aurora.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 花未眠
 * qq配置属性
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "qq")
public class QQConfigProperties {

    /**
     * QQ appId
     */
    private String appId;

    /**
     * 校验token地址
     */
    private String checkTokenUrl;

    /**
     * QQ用户信息地址
     */
    private String userInfoUrl;

}
